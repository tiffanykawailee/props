package com.mihaibojin.prop;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.logging.Level.SEVERE;

import com.mihaibojin.resolvers.PropertyFileResolver;
import com.mihaibojin.resolvers.Resolver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Props implements AutoCloseable {
  private static final Logger log = Logger.getLogger(PropertyFileResolver.class.getName());
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private final Map<String, AbstractProp<?>> boundProps = new ConcurrentHashMap<>();
  private final CountDownLatch latch = new CountDownLatch(1);

  private final List<String> prioritizedResolvers;
  private final Map<String, Resolver> resolvers2;
  private final Duration shutdownGracePeriod;
  private final Duration refreshInterval;

  private Props(
      LinkedHashMap<String, Resolver> resolvers,
      Duration refreshInterval,
      Duration shutdownGracePeriod) {
    resolvers2 = Collections.unmodifiableMap(resolvers);

    // generate a list of resolver IDs, ordered by priority (highest first)
    ArrayList<String> ids = new ArrayList<>(resolvers.keySet());
    Collections.reverse(ids);
    prioritizedResolvers = Collections.unmodifiableList(ids);

    this.refreshInterval = refreshInterval;
    this.shutdownGracePeriod = shutdownGracePeriod;

    // ensure the non-auto-update resolvers have their values loaded
    CompletableFuture.runAsync(
        () -> {
          resolvers2.entrySet().parallelStream().forEach(Props::safeReload);
          latch.countDown();
        });

    // and schedule the update-able ones to run periodically
    // TODO: this can be risky if the Default ForkJoinPool is busy; refactor to use own executor
    executor.scheduleAtFixedRate(
        () -> refreshResolvers(resolvers2), 0, refreshInterval.toSeconds(), TimeUnit.SECONDS);
  }

  /**
   * Safely reload all the values managed by the specified {@link Resolver} and logs any exceptions
   */
  private static Set<String> safeReload(Entry<String, Resolver> res) {
    try {
      return res.getValue().reload();
    } catch (Throwable t) {
      log.log(SEVERE, "Unexpected error reloading props from " + res.getKey(), t);
    }
    return Set.of();
  }

  /** Refreshes values from all the registered {@link Resolver}s */
  private void refreshResolvers(Map<String, Resolver> resolvers) {
    Set<? extends AbstractProp<?>> toUpdate =
        resolvers
            .entrySet()
            .parallelStream()
            .filter(r -> r.getValue().isReloadable())
            .map(Props::safeReload)
            .flatMap(keys -> keys.stream().map(boundProps::get).filter(Objects::nonNull))
            // we need to collect since we need all layers to have finished their update cycle
            // before reading them
            .collect(Collectors.toSet());

    // TODO(mihaibojin): in the future, this will be replace with a better mechanism that keeps
    // track of which resolver owns each prop
    toUpdate.forEach(this::update);
  }

  /**
   * Binds the specified prop to the current {@link Props} object
   *
   * @throws IllegalArgumentException if attempting to bind a {@link Prop} for a key which was
   *     already bound to another object. This is to encourage efficiency and define a single object
   *     per key, keeping memory usage low(er). Use the {@link #retrieve(String)} and {@link
   *     #retrieve(String, Class)} methods to get a pre-existing instance.
   */
  public <T> AbstractProp<T> bind(AbstractProp<T> prop) {
    // TODO(mihaibojin): lazy load, block on get
    update(prop);

    AbstractProp<?> oldProp = boundProps.putIfAbsent(prop.key, prop);
    if (nonNull(oldProp) && oldProp != prop) {
      throw new IllegalArgumentException(
          "Prop with key "
              + prop.key
              + " was already registered via "
              + oldProp.getClass().getSimpleName());
    }

    return prop;
  }

  /**
   * Update the {@link Prop}'s current value
   *
   * @return true if the property was updated, or false if it kept its value
   */
  public <T> boolean update(AbstractProp<T> prop) {
    // load the key
    Optional<T> propValue = resolveProp(prop);
    if (propValue.isEmpty()) {
      return false;
    }

    // retrieve the prop's current value
    T currentValue = propValue.get();

    // determine if the actual value has changed, return otherwise
    if (Objects.equals(prop.value(), currentValue)) {
      return false;
    }

    // update the current value, if necessary
    prop.setValue(currentValue);

    return true;
  }

  /** Search all resolvers for a value */
  <T> Optional<T> resolveProp(AbstractProp<T> prop) {
    if (!waitForInitialLoad()) {
      return Optional.empty();
    }

    for (String id : prioritizedResolvers) {
      Optional<String> value = resolvers2.get(id).get(prop.key());
      if (value.isPresent()) {
        log.log(Level.FINER, format("%s resolved by %s", prop.key(), id));

        // return an optional which decodes the value on get
        // the reason for lazy decoding is to reduce confusion in a potential stacktrace
        // since the problem would be related to decoding the retrieved string and not with
        // resolving the value
        return value.map(prop::decode);
      }
    }

    return Optional.empty();
  }

  /** Only attempt a specific resolver */
  <T> Optional<T> resolveProp(AbstractProp<T> prop, String resolverId) {
    if (!waitForInitialLoad()) {
      return Optional.empty();
    }

    if (!resolvers2.containsKey(resolverId)) {
      throw new IllegalArgumentException(
          "Resolver " + resolverId + " is not registered with the current registry");
    }

    return resolvers2.get(resolverId).get(prop.key).map(prop::decode);
  }

  /**
   * Returns a {@link LinkedHashMap} containing mappings for each resolver which contained a value
   * for the specified {@link Prop}'s key.
   *
   * <p>The map can be iterated over in and will return {@link Resolver}s order by priority,
   * lowest-to-highest.
   */
  public <T> Map<String, T> resolvePropLayers(AbstractProp<T> prop) {
    // read all the values from the registry
    Map<String, T> layers = new LinkedHashMap<>();

    // process all layers and transform them into the final type
    for (Entry<String, Resolver> entry : resolvers2.entrySet()) {
      Optional<String> value = entry.getValue().get(prop.key());
      if (value.isPresent()) {
        T resolved = prop.decode(value.get());
        prop.validateOnSet(resolved);
        layers.put(entry.getKey(), resolved);
      }
    }

    return layers;
  }

  /** @return true if the wait completed successfully */
  private <T> boolean waitForInitialLoad() {
    try {
      // TODO: replace this with a lazy load
      latch.await(refreshInterval.toSeconds(), TimeUnit.SECONDS);
      return true;

    } catch (InterruptedException e) {
      log.log(SEVERE, "Could not resolve in time", e);
      Thread.currentThread().interrupt();
      return false;
    }
  }

  public <T> Builder<T> create(String key, PropTypeConverter<T> converter) {
    return new Builder<>(key, converter);
  }

  /**
   * @return an existing (bound) {@link Prop} object, or <code>null</code> if one does not exist for
   *     the specified key
   */
  public Prop<?> retrieve(String key) {
    return boundProps.get(key);
  }

  /**
   * @throws ClassCastException if the property key is associated with a different type
   * @return an existing (bound) {@link Prop} object, cast to the expected type, or <code>null
   *     </code> if a prop was not bound for the specified key
   */
  public <T> Prop<T> retrieve(String key, Class<Prop<T>> clz) {
    return clz.cast(boundProps.get(key));
  }

  /**
   * Call this method when shutting down the app to stop this class's {@link
   * ScheduledExecutorService}
   */
  @Override
  public void close() {
    executor.shutdown();
    try {
      executor.awaitTermination(shutdownGracePeriod.toSeconds(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.warning("Interrupted while waiting for executor shutdown; terminating...");
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  /** @return a {@link Factory} object used to configure a {@link Props} instance */
  public static Factory factory() {
    return new Factory();
  }

  /** Factory for {@link Props} */
  public static class Factory {
    private final LinkedHashMap<String, Resolver> resolvers = new LinkedHashMap<>();
    private Duration refreshInterval = Duration.ofSeconds(30);
    private Duration shutdownGracePeriod = Duration.ofSeconds(30);

    private Factory() {}

    /** Adds a resolver and its identifier to be used in the registry object being built */
    public Factory withResolver(String id, Resolver resolver) {
      resolvers.put(id, resolver);
      return this;
    }

    /**
     * Allows customizing the refresh interval at which auto-update-able {@link
     * com.mihaibojin.resolvers.Resolver}s are refreshed
     */
    public Factory refreshInterval(Duration interval) {
      refreshInterval = interval;
      return this;
    }

    /**
     * Allows customizing the shutdown grace period, before the executor is forcefully shut down.
     */
    public Factory shutdownGracePeriod(Duration shutdownGracePeriod) {
      this.shutdownGracePeriod = shutdownGracePeriod;
      return this;
    }

    /** Create the {@link Props} object */
    public Props build() {
      return new Props(resolvers, refreshInterval, shutdownGracePeriod);
    }
  }

  /** Wrapper for {@link Resolver}s */
  private static class ResolverWrapper {
    final String id;
    final Resolver resolver;

    private ResolverWrapper(String id, Resolver resolver) {
      this.id = id;
      this.resolver = resolver;
    }

    /** Convenience method for making the API easier to read */
    private static ResolverWrapper of(String id, Resolver resolver) {
      return new ResolverWrapper(id, resolver);
    }

    @Override
    public String toString() {
      return "Resolver{" + id + '}';
    }
  }

  /** Builder class for creating custom {@link Prop}s from the current {@link Props} registry */
  public class Builder<T> {
    public final String key;
    public final PropTypeConverter<T> converter;
    private T defaultValue;
    private String description;
    private boolean isRequired;
    private boolean isSecret;

    private Builder(String key, PropTypeConverter<T> converter) {
      this.key = key;
      this.converter = converter;
    }

    public Builder defaultValue(T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder isRequired(boolean isRequired) {
      this.isRequired = isRequired;
      return this;
    }

    public Builder isSecret(boolean isSecret) {
      this.isSecret = isSecret;
      return this;
    }

    /**
     * Constructs the {@link Prop}, binds it to the current {@link Props} instance, and returns it
     */
    public AbstractProp<T> build() {
      return bind(
          new AbstractProp<>(key, defaultValue, description, isRequired, isSecret) {
            @Override
            public T decode(String value) {
              return converter.decode(value);
            }

            @Override
            public String encode(T value) {
              return converter.encode(value);
            }
          });
    }
  }
}

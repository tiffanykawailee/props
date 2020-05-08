package com.mihaibojin.prop;

import static java.util.Objects.nonNull;
import static java.util.logging.Level.SEVERE;

import com.mihaibojin.prop.types.SimpleProp;
import com.mihaibojin.resolvers.PropertyFileResolver;
import com.mihaibojin.resolvers.Resolver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Props implements AutoCloseable {
  private static final Logger log = Logger.getLogger(PropertyFileResolver.class.getName());
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private final Map<String, AbstractProp> boundProps = new ConcurrentHashMap<>();
  private final CountDownLatch latch = new CountDownLatch(1);

  private final Deque<ResolverWrapper> resolvers = new LinkedList<>();
  private final Duration shutdownGracePeriod;

  private Props(List<ResolverWrapper> resolvers, Duration period, Duration shutdownGracePeriod) {
    // TODO: small optimization, Map<ResolverId, List position>
    this.resolvers.addAll(resolvers);
    this.shutdownGracePeriod = shutdownGracePeriod;

    // ensure the non-auto-update resolvers have their values loaded
    CompletableFuture.runAsync(
        () -> {
          // TODO: handle reload() exceptions
          resolvers.parallelStream().forEach(r -> r.resolver.reload());
          latch.countDown();
        });

    // and schedule the update-able ones to run periodically
    // TODO: this can be risky if the Default ForkJoinPool is busy; refactor to use own executor
    executor.scheduleAtFixedRate(
        () -> refreshResolvers(this.resolvers), 0, period.toSeconds(), TimeUnit.SECONDS);
  }

  private void refreshResolvers(Collection<ResolverWrapper> resolvers) {
    Set<AbstractProp> toUpdate =
        resolvers
            .parallelStream()
            .filter(r -> r.resolver.isReloadable())
            .map(r -> r.resolver.reload())
            .flatMap(keys -> keys.stream().map(boundProps::get).filter(Objects::nonNull))
            .collect(Collectors.toSet());

    // TODO: this is a bit messy, find a better way to notify of changes
    toUpdate.forEach(AbstractProp::update);
  }

  /** Search all resolvers for a value */
  public Map<String, String> get(String key) {
    try {
      // TODO: replace this with a lazy load
      latch.await();
    } catch (InterruptedException e) {
      log.log(SEVERE, "Could not resolve in time", e);
      Thread.currentThread().interrupt();
      return Map.of();
    }

    Map<String, String> layers = new LinkedHashMap<>();

    var it = resolvers.descendingIterator();
    while (it.hasNext()) {
      var binding = it.next();
      Optional<String> value = binding.resolver.get(key);
      value.ifPresent(val -> layers.put(binding.id, val));
    }

    return layers;
  }

  /**
   * Only attempt a specific resolver
   *
   * @return null if a value was not found
   */
  public Map<String, String> get(String key, String resolverId) {
    // TODO: small optimization, Map<ResolverId, List position>
    var it = resolvers.descendingIterator();
    while (it.hasNext()) {
      var binding = it.next();
      if (Objects.equals(binding.id, resolverId)) {
        Optional<String> found = binding.resolver.get(key);
        if (found.isPresent()) {
          return Map.of(binding.id, found.get());
        }
      }
    }

    return Map.of();
  }

  /**
   * Binds the specified prop to the current {@link Props} object
   *
   * <p>Note: you cannot bind more than one {@link AbstractProp} with the same key to a {@link
   * Props} object, however, the prop will get the current registry reference. This means you can
   * define multiple prop implementations for the same key, but only the first will be registered
   * and will get any events.
   */
  public void bind(AbstractProp prop) {
    prop.setRegistry(this);
    prop.update();

    AbstractProp oldProp = boundProps.putIfAbsent(prop.key, prop);
    if (nonNull(oldProp) && oldProp != prop) {
      throw new IllegalArgumentException(
          "Prop with key "
              + prop.key
              + " was already registered via "
              + oldProp.getClass().getSimpleName());
    }
  }

  public <T> AbstractProp<T> create(String key, TypedProp<T> formatter) {
    SimpleProp<T> prop =
        new SimpleProp<>("test.prop", null) {
          @Override
          public T resolveValue(String value) {
            return formatter.resolveValue(value);
          }
        };
    bind(prop);
    return prop;
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
    private final List<ResolverWrapper> resolvers = new ArrayList<>();
    private Duration interval = Duration.ofSeconds(30);
    private Duration shutdownGracePeriod = Duration.ofSeconds(30);

    private Factory() {}

    /** Adds a resolver and its identifier to be used in the registry object being built */
    public Factory withResolver(String id, com.mihaibojin.resolvers.Resolver resolver) {
      resolvers.add(ResolverWrapper.of(id, resolver));
      return this;
    }

    /**
     * Allows customizing the refresh interval at which auto-update-able {@link
     * com.mihaibojin.resolvers.Resolver}s are refreshed
     */
    public Factory refreshInterval(Duration interval) {
      this.interval = interval;
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
      return new Props(resolvers, interval, shutdownGracePeriod);
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
}

package com.mihaibojin.prop;

import com.mihaibojin.logger.LoggerBridge;
import com.mihaibojin.logger.LoggerInterface;
import com.mihaibojin.resolvers.Resolver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PropRegistry implements AutoCloseable {
  private static final LoggerInterface log = LoggerBridge.getLogger();
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  private final Deque<ResolverBinding> resolvers = new LinkedList<>();
  private final Duration shutdownGracePeriod;

  private PropRegistry(
      List<ResolverBinding> resolvers, Duration period, Duration shutdownGracePeriod) {
    // TODO: small optimization, Map<ResolverId, List position>
    this.resolvers.addAll(resolvers);
    this.shutdownGracePeriod = shutdownGracePeriod;

    // ensure the non-auto-update resolvers have their values loaded
    executor.submit(
        () ->
            resolvers
                .parallelStream()
                .filter(r -> !r.resolver.shouldAutoUpdate())
                .forEach(r -> r.resolver.refresh()));

    // and schedule the update-able ones to run periodically
    executor.scheduleAtFixedRate(
        () -> refreshResolvers(this.resolvers), 0, period.toSeconds(), TimeUnit.SECONDS);
  }

  private void refreshResolvers(Collection<ResolverBinding> resolvers) {
    resolvers
        .parallelStream()
        .filter(r -> r.resolver.shouldAutoUpdate())
        .forEach(r -> r.resolver.refresh());
  }

  /**
   * Search all resolvers for a value
   *
   * @return null if a value was not found
   */
  public String get(String key) {
    var it = resolvers.descendingIterator();
    while (it.hasNext()) {
      Optional<String> value = it.next().resolver.get(key);
      if (value.isPresent()) {
        return value.get();
      }
    }

    return null;
  }

  /**
   * Only attempt a specific resolver
   *
   * @return null if a value was not found
   */
  public String get(String key, String resolverId) {
    // TODO: small optimization, Map<ResolverId, List position>
    var it = resolvers.descendingIterator();
    while (it.hasNext()) {
      var binding = it.next();
      if (Objects.equals(binding.id, resolverId)) {
        return binding.resolver.get(key).orElse(null);
      }
    }

    return null;
  }

  /** Binds the specified prop to the current {@link PropRegistry} object */
  public void bind(Prop prop) {
    prop.setRegistry(this);
    prop.update();
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
      log.warn("Interrupted while waiting for executor shutdown; terminating...");
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  /** Builder class for */
  public static class Builder {
    private final List<ResolverBinding> resolvers = new ArrayList<>();
    private Duration interval = Duration.ofSeconds(30);
    private Duration shutdownGracePeriod = Duration.ofSeconds(30);

    /** Adds a resolver and its identifier to be used in the registry object being built */
    public Builder withResolver(String id, Resolver resolver) {
      resolvers.add(new ResolverBinding(id, resolver));
      return this;
    }

    /**
     * Allows customizing the refresh interval at which auto-update-able {@link Resolver}s are
     * refreshed
     */
    public Builder refreshInterval(Duration interval) {
      this.interval = interval;
      return this;
    }

    /**
     * Allows customizing the shutdown grace period, before the executor is forcefully shut down.
     */
    public Builder shutdownGracePeriod(Duration shutdownGracePeriod) {
      this.shutdownGracePeriod = shutdownGracePeriod;
      return this;
    }

    /** Create the {@link PropRegistry} object */
    public PropRegistry build() {
      return new PropRegistry(resolvers, interval, shutdownGracePeriod);
    }
  }
}

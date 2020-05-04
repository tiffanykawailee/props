package com.mihaibojin;

import static java.util.function.Predicate.not;

import com.mihaibojin.contract.Resolver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PropRegistry implements AutoCloseable {
  private final Deque<Resolver> resolvers = new LinkedList<>();
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  private PropRegistry(List<Resolver> resolvers, Duration period) {
    this.resolvers.addAll(resolvers);

    // ensure the non-auto-update resolvers have their values loaded
    executor.submit(
        () ->
            resolvers
                .parallelStream()
                .filter(not(Resolver::shouldAutoUpdate))
                .forEach(Resolver::refresh));

    // and schedule the update-able ones to run periodically
    executor.scheduleAtFixedRate(
        () -> update(this.resolvers), 0, period.toSeconds(), TimeUnit.SECONDS);
  }

  protected void update(Collection<Resolver> resolvers) {
    resolvers.parallelStream().filter(Resolver::shouldAutoUpdate).forEach(Resolver::refresh);
  }

  public String get(String key) {
    var it = resolvers.descendingIterator();
    while (it.hasNext()) {
      Optional<String> value = it.next().get(key);
      if (value.isPresent()) {
        return value.get();
      }
    }

    return null;
  }

  @Override
  public void close() throws Exception {
    executor.shutdown();
    try {
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      executor.shutdownNow();
    }
  }

  public static class Builder {
    private final List<Resolver> resolvers = new ArrayList<>();
    private Duration interval = Duration.ofSeconds(30);

    public Builder withResolver(Resolver resolver) {
      resolvers.add(resolver);
      return this;
    }

    public Builder withResolvers(Resolver... resolvers) {
      this.resolvers.addAll(List.of(resolvers));
      return this;
    }

    public Builder updateInterval(Duration interval) {
      this.interval = interval;
      return this;
    }

    public PropRegistry build() {
      return new PropRegistry(resolvers, interval);
    }
  }
}

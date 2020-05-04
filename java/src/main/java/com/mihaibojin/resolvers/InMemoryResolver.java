package com.mihaibojin.resolvers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Useful for tests, when the implementation requires overriding values. */
public class InMemoryResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();

  public void set(String key, String value) {
    store.put(key, value);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public boolean shouldAutoUpdate() {
    return false;
  }

  @Override
  public void refresh() {
    // do nothing
  }
}

package com.mihaibojin.resolvers;

import com.mihaibojin.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EnvResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();

  @Override
  public boolean shouldAutoUpdate() {
    // the environment is not re-read after the JVM is started
    return false;
  }

  @Override
  public Set<String> refresh() {
    return Utils.mergeMapsInPlace(store, System.getenv());
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }
}

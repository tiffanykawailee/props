package com.mihaibojin.resolvers;

import com.mihaibojin.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SystemPropertyResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public boolean shouldAutoUpdate() {
    // System properties do not need to be read more than once
    return false;
  }

  /** You can call this method manually, after updating the System properties object (if needed) */
  @Override
  public void refresh() {
    Utils.mergeMapsInPlace(store, Utils.readPropertiesToMap(System.getProperties()));
  }
}

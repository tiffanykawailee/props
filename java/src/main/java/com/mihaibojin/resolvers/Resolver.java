package com.mihaibojin.resolvers;

import java.util.Optional;
import java.util.Set;

public interface Resolver {
  Optional<String> get(String key);

  Set<String> refresh();

  boolean shouldAutoUpdate();
}

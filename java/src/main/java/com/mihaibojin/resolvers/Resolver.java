package com.mihaibojin.resolvers;

import java.util.Optional;

public interface Resolver {
  Optional<String> get(String key);

  void refresh();

  boolean shouldAutoUpdate();
}

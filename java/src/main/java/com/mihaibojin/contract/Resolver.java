package com.mihaibojin.contract;

import java.util.Optional;

public interface Resolver {
  boolean shouldAutoUpdate();

  void refresh();

  Optional<String> get(String key);
}

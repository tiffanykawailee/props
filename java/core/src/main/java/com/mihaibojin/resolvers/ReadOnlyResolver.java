package com.mihaibojin.resolvers;

import java.util.Set;

public abstract class ReadOnlyResolver implements Resolver {
  @Override
  public final boolean isReloadable() {
    return false;
  }

  @Override
  public final Set<String> reload() {
    return Set.of();
  }
}

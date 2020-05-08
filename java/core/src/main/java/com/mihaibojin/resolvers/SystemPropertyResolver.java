package com.mihaibojin.resolvers;

import java.util.Optional;

/** Loads system properties */
public class SystemPropertyResolver extends ReadOnlyResolver {
  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(System.getProperty(key));
  }
}

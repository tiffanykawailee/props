package com.mihaibojin.props.core.resolvers;

import java.util.Optional;

/** Loads values defined in the environment */
public class EnvResolver extends ReadOnlyResolver {
  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(System.getenv(key));
  }

  @Override
  public String defaultId() {
    return "ENV";
  }
}

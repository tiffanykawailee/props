package com.mihaibojin.prop;

import com.mihaibojin.resolvers.Resolver;

@FunctionalInterface
public interface TypedProp<T> {
  /** Parses the string value provided by a {@link Resolver} into the final type */
  T resolveValue(String value);
}

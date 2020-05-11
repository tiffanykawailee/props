package com.mihaibojin.props.core.resolvers;

import java.util.Optional;
import java.util.Set;

public interface Resolver {

  /** @return the value of the specified key, or <code>null</code> if the property is not defined */
  Optional<String> get(String key);

  /**
   * Reloads all properties managed by the implementing Resolver.
   *
   * @return a {@link Set} of all the property keys updated by the last execution of this method
   */
  Set<String> reload();

  /**
   * @return <code>true</code> if the implementation can reload its properties, or <code>false
   *     </code> if it cannot
   */
  default boolean isReloadable() {
    return true;
  }
}

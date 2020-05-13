/*
 * Copyright 2020 Mihai Bojin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mihaibojin.props.core.resolvers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Useful for tests, when the implementation requires overriding values. */
public class InMemoryResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();

  public void set(String key, String value) {
    store.put(key, value);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public boolean isReloadable() {
    return false;
  }

  @Override
  public Set<String> reload() {
    return Set.of();
  }

  @Override
  public String defaultId() {
    return "MEMORY";
  }
}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class ResolverUtils {
  private static final Logger log = Logger.getLogger(ResolverUtils.class.getName());

  /**
   * Loads a {@link Properties} object from the passed {@link InputStream} and returns a {@link Map}
   * containing all key->value mappings.
   *
   * @throws NullPointerException if a null <code>InputStream</code> was passed
   * @throws IOException if the <code>InputStream</code> cannot be read
   */
  public static Map<String, String> loadPropertiesFromStream(InputStream stream)
      throws IOException {
    Properties properties = new Properties();
    properties.load(Objects.requireNonNull(stream));
    return readPropertiesToMap(properties);
  }

  /**
   * Iterates over all the input {@link Properties} and returns a {@link Map} containing all
   * key->value mappings.
   */
  private static Map<String, String> readPropertiesToMap(Properties properties) {
    Map<String, String> store = new HashMap<>();
    for (String key : properties.stringPropertyNames()) {
      store.put(key, properties.getProperty(key));
    }
    return store;
  }

  /**
   * Merges the <code>collector</code> and <code>updated</code> maps by.
   * <li/>- deleting any keys which are no longer defined in <code>updated</code>
   * <li/>- updating any keys whose values have changed in <code>updated</code>
   * <li/>- setting any new keys whose values have been added in <code>updated</code>
   *
   * @return the {@link Set} of new, updated, and deleted keys
   */
  public static Set<String> mergeMapsInPlace(
      Map<String, String> collector, Map<String, String> updated) {
    var toDel = new HashSet<String>();

    // if the key doesn't exist in the updated set, mark it for deletion
    for (Entry<String, String> val : collector.entrySet()) {
      if (!updated.containsKey(val.getKey())) {
        toDel.add(val.getKey());
      }
    }

    // delete all keys which do not appear in the updated list
    for (String key : toDel) {
      collector.remove(key);
    }

    // set all updated values
    for (Entry<String, String> newVal : updated.entrySet()) {
      if (!Objects.equals(collector.get(newVal.getKey()), newVal.getValue())) {
        collector.put(newVal.getKey(), newVal.getValue());
        // store each updated key into the same 'toDel' set, to avoid creating a new object
        toDel.add(newVal.getKey());
      }
    }

    // return all deleted, new, and updated keys
    return toDel;
  }

  /**
   * Helper method that takes a random input string and returns all words uppercased and separated
   * by a single underscore character.
   */
  public static String formatResolverId(String location) {
    return location.toUpperCase().replaceAll("[^A-Z]+", "_");
  }
}

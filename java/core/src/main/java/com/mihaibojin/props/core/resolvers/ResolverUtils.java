package com.mihaibojin.props.core.resolvers;

import static java.util.Objects.isNull;

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
   * <p>This method will ignore <code>null</code> <code>InputStream</code>s.
   *
   * @throws IOException if the <code>InputStream</code> cannot be read.
   */
  public static Map<String, String> loadPropertiesFromStream(InputStream stream)
      throws IOException {
    if (isNull(stream)) {
      log.info("A null InputStream was passed; skipping it...");
      return null;
    }

    Properties properties = new Properties();
    properties.load(stream);
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
   * Merges the <code>collector</code> and <code>updated</code> maps by:
   * <li>- deleting any keys which are no longer defined in <code>updated</code>
   * <li>- updating any keys whose values have changed in <code>updated</code>
   * <li>- setting any new keys whose values have been added in <code>updated</code>
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
}

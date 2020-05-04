package com.mihaibojin;

import static java.util.Objects.isNull;

import com.mihaibojin.logger.LoggerBridge;
import com.mihaibojin.logger.LoggerInterface;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.SortedMap;
import java.util.regex.Pattern;

public class Utils {
  private static final LoggerInterface log = LoggerBridge.getLogger();

  public static Number parseNumber(String value) {
    try {
      return NumberFormat.getInstance().parse(value);
    } catch (ParseException e) {
      log.error("Could not parse " + value + " to a number", e);
      return null;
    }
  }

  public static Map<String, String> readPropertiesToMap(Properties properties) {
    Map<String, String> store = new HashMap<>();
    for (String key : properties.stringPropertyNames()) {
      store.put(key, properties.getProperty(key));
    }
    return store;
  }

  public static Optional<Integer> getInt(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::parseNumber).map(Number::intValue);
  }

  public static Optional<Long> getLong(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::parseNumber).map(Number::longValue);
  }

  public static Optional<Float> getFloat(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::parseNumber).map(Number::floatValue);
  }

  public static Optional<Double> getDouble(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::parseNumber).map(Number::doubleValue);
  }

  public static Optional<String> getString(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key));
  }

  public static Optional<List<String>> getStringList(
      Map<String, String> store, String key, String separator) {
    return Optional.ofNullable(store.get(key)).map(value -> splitString(value, separator));
  }

  private static List<String> splitString(String input, String separator) {
    return List.of(input.split(Pattern.quote(separator)));
  }

  public static Map<String, String> loadPropertiesFromStream(InputStream stream)
      throws IOException {
    if (isNull(stream)) {
      log.debug("Cannot load properties from null InputStream");
      return null;
    }

    Properties properties = new Properties();
    properties.load(stream);

    var store = new HashMap<String, String>();
    for (String key : properties.stringPropertyNames()) {
      store.put(key, properties.getProperty(key));
    }
    return store;
  }

  public static void mergeMapsInPlace(Map<String, String> collector, Map<String, String> updated) {
    var toDel = new HashSet<String>();

    // if the key doesn't exist in the updated set, mark it for deletion
    for (Entry<String, String> val : collector.entrySet()) {
      if (!updated.containsKey(val.getKey())) {
        toDel.add(val.getKey());
      }
    }

    // set all updated values
    for (Entry<String, String> newVal : updated.entrySet()) {
      if (!Objects.equals(collector.get(newVal.getKey()), newVal.getValue())) {
        collector.put(newVal.getKey(), newVal.getValue());
      }
    }

    // delete all keys which do not appear in the updated list
    for (String key : toDel) {
      collector.remove(key);
    }
  }

  @Deprecated
  public static void mergeMapsInPlace(
      SortedMap<String, String> collector, SortedMap<String, String> updated) {
    var left = collector.entrySet().iterator();
    var right = updated.entrySet().iterator();

    var toDel = new HashSet<String>();

    while (left.hasNext() && right.hasNext()) {
      Entry<String, String> oldVal = left.next();
      Entry<String, String> newVal = right.next();

      // the old key doesn't exist in the updated Map
      if (oldVal.getKey().compareTo(newVal.getKey()) < 0) {
        toDel.add(oldVal.getKey());
        left.next();
        continue;
      }

      // the updated key is not yet defined in the collector Map, add it
      if (oldVal.getKey().compareTo(newVal.getKey()) > 0) {
        collector.put(newVal.getKey(), newVal.getValue());
        right.next();
        continue;
      }

      // the value was updated, so set it
      if (!Objects.equals(oldVal.getValue(), newVal.getValue())) {
        collector.put(newVal.getKey(), newVal.getValue());
      }

      // advance both cursors
      left.next();
      right.next();
    }

    // delete any items that were left behind
    while (left.hasNext()) {
      toDel.add(left.next().getKey());
    }

    // set any new items
    while (right.hasNext()) {
      var newVal = right.next();
      collector.put(newVal.getKey(), newVal.getValue());
    }

    for (String key : toDel) {
      collector.remove(key);
    }
  }
}

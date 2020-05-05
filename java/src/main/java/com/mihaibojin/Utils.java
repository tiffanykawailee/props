package com.mihaibojin;

import static java.util.Objects.isNull;

import com.mihaibojin.logger.LoggerBridge;
import com.mihaibojin.logger.LoggerInterface;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class Utils {
  private static final LoggerInterface log = LoggerBridge.getLogger();

  public static Instant parseInstant(String value) {
    try {
      return OffsetDateTime.parse(value).toInstant();
    } catch (DateTimeParseException e) {
      log.error("Could not parse " + value + " as a valid date/time", e);
      return null;
    }
  }

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

  public static Optional<Instant> getInstant(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::parseInstant);
  }

  public static Optional<Date> getDate(Map<String, String> store, String key) {
    return getInstant(store, key).map(Date::from);
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
        // store each updated key
        toDel.add(newVal.getKey());
      }
    }

    // return all deleted, new, and updated keys
    return toDel;
  }
}

package java.src.main.java.com.mihaibojin;

import static java.util.function.Predicate.not;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
  public static Number parseNumber(String value) {
    try {
      return NumberFormat.getInstance().parse(value);
    } catch (ParseException e) {
      // TODO: integrate with a logging provider
      return null;
    }
  }

  public static void updateMapWithProperties(Map<String, String> store, Properties properties) {
    for (String key : properties.stringPropertyNames()) {
      store.put(key, properties.getProperty(key));
    }
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

  public static Optional<List<String>> getStringList(Map<String, String> store, String key) {
    return Optional.ofNullable(store.get(key)).map(Utils::splitString);
  }

  private static List<String> splitString(String input) {
    return List.of(input.split("[,]+"));
  }
}

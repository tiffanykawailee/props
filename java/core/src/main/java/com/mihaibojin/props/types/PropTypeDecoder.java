package com.mihaibojin.props.types;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropTypeDecoder {
  private static final Logger log = Logger.getLogger(PropTypeDecoder.class.getName());

  /**
   * Constructs a converter which returns the inputted {@link String}, ensuring it is not <code>null
   * </code>
   */
  public static PropTypeConverter<String> asString() {
    return Objects::requireNonNull;
  }

  /** Constructs a converter which splits the inputted {@link String} into a {@link List} */
  public static PropTypeConverter<List<String>> asStringList(String separator) {
    return (value) -> splitString(requireNonNull(value), separator);
  }

  /** Constructs a converter which casts the inputted {@link String} to a {@link Boolean} value */
  public static PropTypeConverter<Boolean> asBoolean() {
    return Boolean::parseBoolean;
  }

  /** Constructs a converter which casts the inputted {@link String} to an {@link Integer} value */
  public static PropTypeConverter<Integer> asInteger() {
    return (value) -> requireNonNull(safeParseNumber(value)).intValue();
  }

  /**
   * Constructs a converter which splits the inputted {@link String} into a {@link List} of {@link
   * Integer}s
   */
  public static PropTypeConverter<List<Integer>> asIntegerList(String separator) {
    return (value) -> splitStringAsNumbers(value, separator, Number::intValue);
  }

  /** Constructs a converter which casts the inputted {@link String} to a {@link Long} value */
  public static PropTypeConverter<Long> asLong() {
    return (value) -> requireNonNull(safeParseNumber(value)).longValue();
  }

  /**
   * Constructs a converter which splits the inputted {@link String} into a {@link List} of {@link
   * Long}s
   */
  public static PropTypeConverter<List<Long>> asLongList(String separator) {
    return (value) -> splitStringAsNumbers(value, separator, Number::longValue);
  }

  /** Constructs a converter which casts the inputted {@link String} to a {@link Float} value */
  public static PropTypeConverter<Float> asFloat() {
    return (value) -> requireNonNull(safeParseNumber(value)).floatValue();
  }

  /**
   * Constructs a converter which splits the inputted {@link String} into a {@link List} of {@link
   * Float}s
   */
  public static PropTypeConverter<List<Float>> asFloatList(String separator) {
    return (value) -> splitStringAsNumbers(value, separator, Number::floatValue);
  }

  /** Constructs a converter which casts the inputted {@link String} to a {@link Double} value */
  public static PropTypeConverter<Double> asDouble() {
    return (value) -> requireNonNull(safeParseNumber(value)).doubleValue();
  }

  /**
   * Constructs a converter which splits the inputted {@link String} into a {@link List} of {@link
   * Double}s
   */
  public static PropTypeConverter<List<Double>> asDoubleList(String separator) {
    return (value) -> splitStringAsNumbers(value, separator, Number::doubleValue);
  }

  /** Constructs a converter which casts the inputted {@link String} to an {@link Instant} value */
  public static PropTypeConverter<Instant> asInstant() {
    return (value) -> requireNonNull(safeParseInstant(value));
  }

  /** Constructs a converter which casts the inputted {@link String} to an {@link Instant} value */
  public static PropTypeConverter<Date> asDate() {
    return (value) -> Date.from(requireNonNull(safeParseInstant(value)));
  }

  /**
   * Attempts to parse a {@link String} to a {@link Number} and returns <code>null</code> if it
   * cannot.
   *
   * <p>This methods logs a {@link java.util.logging.Level#SEVERE} event instead of throwing {@link
   * ParseException}s.
   */
  public static Number safeParseNumber(String value) {
    try {
      return NumberFormat.getInstance().parse(value);
    } catch (ParseException e) {
      log.log(SEVERE, "Could not parse " + value + " to a number", e);
      return null;
    }
  }

  /**
   * Attempts to parse a {@link String} to an {@link Instant} and returns <code>null</code> if it
   * cannot.
   *
   * <p>This methods logs a {@link java.util.logging.Level#SEVERE} event instead of throwing {@link
   * DateTimeParseException}s.
   */
  public static Instant safeParseInstant(String value) {
    try {
      return OffsetDateTime.parse(value).toInstant();
    } catch (DateTimeParseException e) {
      log.log(SEVERE, "Could not parse " + value + " as a valid date/time", e);
      return null;
    }
  }

  /** Splits a {@link String} by the given <code>separator</code> */
  private static List<String> splitString(String input, String separator) {
    return List.of(input.split(Pattern.quote(separator)));
  }

  /**
   * Splits a {@link String} by the given <code>separator</code>, casts every item using the
   * specified <code>mapper</code> func and returns a {@link List} of numbers
   */
  private static <T extends Number> List<T> splitStringAsNumbers(
      String input, String separator, Function<Number, T> mapper) {
    return Stream.of(input.split(Pattern.quote(separator)))
        .map(PropTypeDecoder::safeParseNumber)
        .filter(Objects::nonNull)
        .map(mapper)
        .collect(Collectors.toList());
  }
}

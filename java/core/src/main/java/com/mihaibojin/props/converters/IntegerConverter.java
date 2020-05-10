package com.mihaibojin.props.converters;

import static com.mihaibojin.props.converters.ConverterUtils.safeParseNumber;
import static java.util.Objects.requireNonNull;

/** Converter that casts the inputted {@link String} to an {@link Integer} value */
public interface IntegerConverter extends PropTypeConverter<Integer> {
  @Override
  default Integer decode(String value) {
    return requireNonNull(safeParseNumber(value)).intValue();
  }
}

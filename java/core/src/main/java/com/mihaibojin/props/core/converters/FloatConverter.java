package com.mihaibojin.props.core.converters;

import static com.mihaibojin.props.core.converters.ConverterUtils.safeParseNumber;
import static java.util.Objects.requireNonNull;

/** Converter that casts the inputted {@link String} to an {@link Float} value */
public interface FloatConverter extends PropTypeConverter<Float> {
  @Override
  default Float decode(String value) {
    return requireNonNull(safeParseNumber(value)).floatValue();
  }
}

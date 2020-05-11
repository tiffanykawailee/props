package com.mihaibojin.props.core.converters;

import static com.mihaibojin.props.core.converters.ConverterUtils.safeParseNumber;
import static java.util.Objects.requireNonNull;

/** Converter that casts the inputted {@link String} to an {@link Long} value */
public interface LongConverter extends PropTypeConverter<Long> {
  @Override
  default Long decode(String value) {
    return requireNonNull(safeParseNumber(value)).longValue();
  }
}

package com.mihaibojin.props.converters;

import static com.mihaibojin.props.converters.ConverterUtils.safeParseInstant;

import java.time.Instant;

/** Converter that casts the inputted {@link String} to an {@link Instant} value */
public interface InstantConverter extends PropTypeConverter<Instant> {
  @Override
  default Instant decode(String value) {
    return safeParseInstant(value);
  }
}

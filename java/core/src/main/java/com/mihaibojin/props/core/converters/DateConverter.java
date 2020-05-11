package com.mihaibojin.props.core.converters;

import static com.mihaibojin.props.core.converters.ConverterUtils.safeParseInstant;

import java.util.Date;
import java.util.Optional;

/** Converter that casts the inputted {@link String} to an {@link Date} value */
public interface DateConverter extends PropTypeConverter<Date> {
  @Override
  default Date decode(String value) {
    return Optional.ofNullable(safeParseInstant(value)).map(Date::from).orElse(null);
  }
}

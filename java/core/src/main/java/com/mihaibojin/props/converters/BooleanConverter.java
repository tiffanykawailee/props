package com.mihaibojin.props.converters;

/** Converter that casts the inputted {@link String} to a {@link Boolean} value */
public interface BooleanConverter extends PropTypeConverter<Boolean> {
  @Override
  default Boolean decode(String value) {
    return Boolean.parseBoolean(value);
  }
}

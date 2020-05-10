package com.mihaibojin.props.converters;

/** Converter that returns the inputted {@link String} */
public interface StringConverter extends PropTypeConverter<String> {
  @Override
  default String decode(String value) {
    return value;
  }
}

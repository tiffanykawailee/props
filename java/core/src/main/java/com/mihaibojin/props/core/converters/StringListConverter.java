package com.mihaibojin.props.core.converters;

import static com.mihaibojin.props.core.converters.ConverterUtils.splitString;
import static java.util.Objects.requireNonNull;

import java.util.List;

/**
 * Converter that splits the inputted {@link String} into a {@link List} of {@link String}s. The
 * separator can be configured by overriding {@link ListConverter#separator()}.
 */
public interface StringListConverter extends PropTypeConverter<List<String>>, ListConverter {
  @Override
  default List<String> decode(String value) {
    return splitString(requireNonNull(value), separator());
  }
}

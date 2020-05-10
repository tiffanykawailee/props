package com.mihaibojin.props.converters;

import static com.mihaibojin.props.converters.ConverterUtils.splitStringAsNumbers;

import java.util.List;

/**
 * Converter that splits the inputted {@link String} into a {@link List} of {@link Integer}s. The
 * separator can be configured by overriding {@link ListConverter#separator()}.
 */
public interface IntegerListConverter extends PropTypeConverter<List<Integer>>, ListConverter {
  @Override
  default List<Integer> decode(String value) {
    return splitStringAsNumbers(value, separator(), Number::intValue);
  }
}

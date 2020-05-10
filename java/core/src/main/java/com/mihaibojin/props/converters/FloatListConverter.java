package com.mihaibojin.props.converters;

import static com.mihaibojin.props.converters.ConverterUtils.splitStringAsNumbers;

import java.util.List;

/**
 * Converter that splits the inputted {@link String} into a {@link List} of {@link Float}s. The
 * separator can be configured by overriding {@link ListConverter#separator()}.
 */
public interface FloatListConverter extends PropTypeConverter<List<Float>>, ListConverter {
  @Override
  default List<Float> decode(String value) {
    return splitStringAsNumbers(value, separator(), Number::floatValue);
  }
}

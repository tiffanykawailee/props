package com.mihaibojin.props.converters;

import static com.mihaibojin.props.converters.ConverterUtils.splitStringAsNumbers;

import java.util.List;

/**
 * Converter that splits the inputted {@link String} into a {@link List} of {@link Double}s. The
 * separator can be configured by overriding {@link ListConverter#separator()}.
 */
public interface DoubleListConverter extends PropTypeConverter<List<Double>>, ListConverter {
  @Override
  default List<Double> decode(String value) {
    return splitStringAsNumbers(value, separator(), Number::doubleValue);
  }
}

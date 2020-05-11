package com.mihaibojin.props.core.converters;

import static com.mihaibojin.props.core.converters.ConverterUtils.splitStringAsNumbers;

import java.util.List;

/**
 * Converter that splits the inputted {@link String} into a {@link List} of {@link Long}s. The
 * separator can be configured by overriding {@link ListConverter#separator()}.
 */
public interface LongListConverter extends PropTypeConverter<List<Long>>, ListConverter {
  @Override
  default List<Long> decode(String value) {
    return splitStringAsNumbers(value, separator(), Number::longValue);
  }
}

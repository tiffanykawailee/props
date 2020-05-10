package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.IntegerListConverter;
import com.mihaibojin.props.core.AbstractProp;
import java.util.List;

public abstract class AbstractIntegerListProp extends AbstractProp<List<Integer>>
    implements IntegerListConverter {
  protected AbstractIntegerListProp(
      String key,
      List<Integer> defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

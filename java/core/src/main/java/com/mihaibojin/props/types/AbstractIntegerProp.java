package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.IntegerConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractIntegerProp extends AbstractProp<Integer>
    implements IntegerConverter {
  protected AbstractIntegerProp(
      String key, Integer defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

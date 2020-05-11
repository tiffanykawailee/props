package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.IntegerConverter;

public abstract class AbstractIntegerProp extends AbstractProp<Integer>
    implements IntegerConverter {
  protected AbstractIntegerProp(
      String key, Integer defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

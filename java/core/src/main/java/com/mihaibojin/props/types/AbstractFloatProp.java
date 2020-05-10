package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.FloatConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractFloatProp extends AbstractProp<Float> implements FloatConverter {
  protected AbstractFloatProp(
      String key, Float defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

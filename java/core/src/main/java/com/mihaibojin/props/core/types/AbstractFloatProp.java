package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.FloatConverter;

public abstract class AbstractFloatProp extends AbstractProp<Float> implements FloatConverter {
  protected AbstractFloatProp(
      String key, Float defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

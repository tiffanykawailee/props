package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.DoubleConverter;

public abstract class AbstractDoubleProp extends AbstractProp<Double> implements DoubleConverter {
  protected AbstractDoubleProp(
      String key, Double defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

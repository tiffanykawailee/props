package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.DoubleConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractDoubleProp extends AbstractProp<Double> implements DoubleConverter {
  protected AbstractDoubleProp(
      String key, Double defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

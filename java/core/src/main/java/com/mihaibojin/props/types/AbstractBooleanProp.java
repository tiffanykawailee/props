package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.BooleanConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractBooleanProp extends AbstractProp<Boolean>
    implements BooleanConverter {
  protected AbstractBooleanProp(
      String key, Boolean defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

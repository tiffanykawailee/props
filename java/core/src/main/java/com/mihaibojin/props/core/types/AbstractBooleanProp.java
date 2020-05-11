package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.BooleanConverter;

public abstract class AbstractBooleanProp extends AbstractProp<Boolean>
    implements BooleanConverter {
  protected AbstractBooleanProp(
      String key, Boolean defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

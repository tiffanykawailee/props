package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.StringConverter;

public abstract class AbstractStringProp extends AbstractProp<String> implements StringConverter {
  protected AbstractStringProp(
      String key, String defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

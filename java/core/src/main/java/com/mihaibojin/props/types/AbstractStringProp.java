package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.StringConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractStringProp extends AbstractProp<String> implements StringConverter {
  protected AbstractStringProp(
      String key, String defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.LongConverter;
import com.mihaibojin.props.core.AbstractProp;

public abstract class AbstractLongProp extends AbstractProp<Long> implements LongConverter {
  protected AbstractLongProp(
      String key, Long defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

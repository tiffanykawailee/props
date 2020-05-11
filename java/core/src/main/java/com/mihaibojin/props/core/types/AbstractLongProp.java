package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.LongConverter;

public abstract class AbstractLongProp extends AbstractProp<Long> implements LongConverter {
  protected AbstractLongProp(
      String key, Long defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

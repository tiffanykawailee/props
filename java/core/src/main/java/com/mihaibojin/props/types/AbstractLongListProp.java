package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.LongListConverter;
import com.mihaibojin.props.core.AbstractProp;
import java.util.List;

public abstract class AbstractLongListProp extends AbstractProp<List<Long>>
    implements LongListConverter {
  protected AbstractLongListProp(
      String key,
      List<Long> defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.StringListConverter;
import java.util.List;

public abstract class AbstractStringListProp extends AbstractProp<List<String>>
    implements StringListConverter {
  protected AbstractStringListProp(
      String key,
      List<String> defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

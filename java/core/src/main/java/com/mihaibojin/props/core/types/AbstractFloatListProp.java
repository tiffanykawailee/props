package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.FloatListConverter;
import java.util.List;

public abstract class AbstractFloatListProp extends AbstractProp<List<Float>>
    implements FloatListConverter {
  protected AbstractFloatListProp(
      String key,
      List<Float> defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

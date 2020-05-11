package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.DoubleListConverter;
import java.util.List;

public abstract class AbstractDoubleListProp extends AbstractProp<List<Double>>
    implements DoubleListConverter {
  protected AbstractDoubleListProp(
      String key,
      List<Double> defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.converters.DateConverter;
import java.util.Date;

public abstract class AbstractDateProp extends AbstractProp<Date> implements DateConverter {
  protected AbstractDateProp(
      String key, Date defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

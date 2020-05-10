package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.DateConverter;
import com.mihaibojin.props.core.AbstractProp;
import java.util.Date;

public abstract class AbstractDateProp extends AbstractProp<Date> implements DateConverter {
  protected AbstractDateProp(
      String key, Date defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

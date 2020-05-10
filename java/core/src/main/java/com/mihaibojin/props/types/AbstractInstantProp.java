package com.mihaibojin.props.types;

import com.mihaibojin.props.converters.InstantConverter;
import com.mihaibojin.props.core.AbstractProp;
import java.time.Instant;

public abstract class AbstractInstantProp extends AbstractProp<Instant>
    implements InstantConverter {
  protected AbstractInstantProp(
      String key, Instant defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

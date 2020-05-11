package com.mihaibojin.props.core;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import com.mihaibojin.props.core.converters.PropTypeConverter;

public abstract class AbstractProp<T> implements Prop<T>, PropTypeConverter<T> {
  public final String key;
  private final T defaultValue;
  private final String description;
  private final boolean isRequired;
  private final boolean isSecret;

  private volatile T currentValue;

  /** @throws IllegalStateException if the constructed object is in an invalid state */
  protected AbstractProp(
      String key, T defaultValue, String description, boolean isRequired, boolean isSecret) {
    this.key = key;
    if (isNull(key)) {
      throw new IllegalStateException("The prop's key cannot be null");
    }

    this.defaultValue = defaultValue;
    this.description = description;
    this.isRequired = isRequired;
    this.isSecret = isSecret;

    // pre-initialize the prop to its default value
    currentValue = defaultValue;
  }

  /**
   * Validates any updates to a property's value.
   *
   * <p>This method can be overridden for more advanced validation requirements.
   */
  protected void validateBeforeSet(T value) {
    if (isRequired && isNull(value)) {
      throw new IllegalStateException(
          format("Prop '%s' is required, but a value was not specified", key));
    }
  }

  /** Update this property's value */
  void setValue(T currentValue) {
    synchronized (this) {
      this.currentValue = currentValue;
    }
  }

  /** @return the current value */
  @Override
  public T value() {
    return currentValue;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public T defaultValue() {
    return defaultValue;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public boolean isRequired() {
    return isRequired;
  }

  @Override
  public boolean isSecret() {
    return isSecret;
  }

  @Override
  public String toString() {
    if (isNull(currentValue)) {
      return format("Prop{%s=null}", key);
    }

    if (isSecret) {
      return format("Prop{%s=(%s)<redacted>}", key, currentValue.getClass().getSimpleName());
    }

    return format("Prop{%s=(%s)%s}", key, currentValue.getClass().getSimpleName(), currentValue);
  }
}

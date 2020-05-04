package com.mihaibojin.prop;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class Prop<T> {
  private final String key;
  private final Class<T> type;
  private final T defaultValue;
  private final String description;
  private final boolean isRequired;
  private final boolean isSecret;
  private final boolean isCacheable;
  private final boolean isDeprecated;
  private T currentValue;

  public Prop(
      String key,
      Class<T> type,
      T defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret,
      boolean isCacheable,
      boolean isDeprecated) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
    this.description = description;
    this.isRequired = isRequired;
    this.isSecret = isSecret;
    this.isCacheable = isCacheable;
    this.isDeprecated = isDeprecated;

    // pre-initialize the prop to its default value
    currentValue = defaultValue;

    internalValidation();
  }

  /**
   * Validates the AppConf
   *
   * @throws IllegalStateException if the constructed object is in an invalid state
   */
  private void internalValidation() {
    if (isNull(key)) {
      throw new IllegalStateException("The prop's key cannot be null");
    }
  }

  protected void validateOnSet() {
    if (isRequired && isNull(defaultValue)) {
      throw new IllegalStateException(
          format("Prop '%s' is required, but a default value was not specified", key));
    }
  }

  @Override
  public String toString() {
    return format("Prop{%s=(%s)%s}", key, type.getSimpleName(), currentValue);
  }
}

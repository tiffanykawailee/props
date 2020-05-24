/*
 * Copyright 2020 Mihai Bojin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mihaibojin.props.core;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import java.util.Optional;

public abstract class AbstractProp<T> implements Prop<T> {
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
    this.defaultValue = defaultValue;
    if (isNull(key)) {
      throw new IllegalStateException("The prop's key cannot be null");
    }

    this.description = description;
    this.isRequired = isRequired;
    this.isSecret = isSecret;
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
  void setValue(T updateValue) {
    // choose the updated value, the default value (if specified), or null, in order
    T val = Optional.ofNullable(updateValue).orElse(defaultValue);

    // ensure the value is validated before it is set
    validateBeforeSet(val);

    synchronized (this) {
      currentValue = val;
    }
  }

  /**
   * This method will return an empty <code>Optional</code> if called on a {@link Prop} which was
   * not bound to a {@link Props} registry.
   *
   * @return an {@link Optional} representing the current value
   */
  @Override
  public Optional<T> value() {
    return Optional.ofNullable(currentValue);
  }

  @Override
  public String key() {
    return key;
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

  /** Helper method for redacting secret values */
  protected String redact(T value) {
    return "<redacted>";
  }

  @Override
  public String toString() {
    if (isNull(currentValue)) {
      return format("Prop{%s=null}", key);
    }

    return format(
        "Prop{%s=(%s)%s}",
        key,
        currentValue.getClass().getSimpleName(),
        isSecret() ? redact(currentValue) : currentValue);
  }
}

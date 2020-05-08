package com.mihaibojin.prop;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public abstract class AbstractProp<T> implements Prop<T>, PropCoder<T> {
  public final String key;
  private final T defaultValue;
  private final String description;
  private final boolean isRequired;
  private final boolean isSecret;
  protected Props registry;

  private Map<String, T> layers;
  private T currentValue;

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

  /** @return a valid {@link Props} object which can satisfy this Prop */
  protected Props registry() {
    return registry;
  }

  /** Convenience method for setting the {@link Props} tied to the current Prop */
  void setRegistry(Props registry) {
    if (nonNull(this.registry)) {
      throw new IllegalStateException("The prop can only be bound to a single Registry");
    }

    this.registry = registry;
  }

  /** @return the current value */
  public T value() {
    return currentValue;
  }

  /**
   * Update the current Prop's value
   *
   * @return true if the property was updated, or false if it kepts its value
   */
  public final boolean update() {
    // read all the values from the registry
    // TODO: this logic should be tied to the registry not the prop; also tie props to a layer
    Map<String, String> values = registry().get(key);

    // process all layers and transform them into the final type
    Map<String, T> layers = new LinkedHashMap<>();
    for (Entry<String, String> entry : values.entrySet()) {
      T resolved = decode(entry.getValue());
      validate(resolved);
      layers.put(entry.getKey(), resolved);
    }

    // store all the layers
    synchronized (this) {
      this.layers = layers;
    }

    // read the top-most value (highest priority)
    T currentValue = null;
    for (Entry<String, T> entry : layers.entrySet()) {
      currentValue = entry.getValue();
      break;
    }

    // determine if the actual value has changed, return otherwise
    if (Objects.equals(this.currentValue, currentValue)) {
      return false;
    }

    // update the current value, if necessary
    this.currentValue = currentValue;
    return true;
  }

  /**
   * Validates the property
   *
   * <p>This method can be overridden for more advanced validation requirements.
   */
  protected void validate(T value) {
    if (isRequired && isNull(value)) {
      throw new IllegalStateException(
          format("Prop '%s' is required, but a value was not specified", key));
    }
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
}

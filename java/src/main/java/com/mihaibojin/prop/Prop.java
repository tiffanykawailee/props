package com.mihaibojin.prop;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.mihaibojin.resolvers.Resolver;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public abstract class Prop<T> {
  public final String key;
  public final Class<T> type;
  private final T defaultValue;
  private final String description;
  private final boolean isRequired;
  private final boolean isSecret;
  protected PropRegistry registry;
  protected String resolverId;

  private Map<String, T> layers;
  private T currentValue;

  public Prop(
      String key,
      Class<T> type,
      T defaultValue,
      String description,
      boolean isRequired,
      boolean isSecret) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
    this.description = description;
    this.isRequired = isRequired;
    this.isSecret = isSecret;

    // pre-initialize the prop to its default value
    currentValue = defaultValue;

    internalValidation();
  }

  /** Parses the string value provided by a {@link Resolver} into the final type */
  protected abstract T resolveValue(String value);

  /** @return a valid {@link PropRegistry} object which can satisfy this Prop */
  protected PropRegistry registry() {
    return registry;
  }

  /** Convenience method for setting the {@link PropRegistry} tied to the current Prop */
  void setRegistry(PropRegistry registry) {
    if (nonNull(this.registry)) {
      throw new IllegalStateException("The prop can only be bound to a single Registry");
    }

    this.registry = registry;
  }

  /**
   * @return {@link String} representing a specific {@link Resolver} or null when all registered
   *     resolvers should be queried.
   */
  protected String resolverId() {
    return resolverId;
  }

  /**
   * In instances where a {@link Prop} is tied to a specific {@link Resolver}, call this method to
   * set a valid resolver id (one that was added to the {@link #registry()})
   */
  void setResolverId(String id) {
    if (nonNull(resolverId)) {
      throw new IllegalStateException("The resolver id can only be set once");
    }

    resolverId = id;
  }

  /**
   * Update the current Prop's value
   *
   * @return true if the property was updated, or false if it kepts its value
   */
  public final boolean update() {
    // read all the values from the registry
    Map<String, String> values;
    String resolverId = resolverId();
    if (isNull(resolverId)) {
      values = registry().get(key);
    } else {
      values = registry().get(key, resolverId);
    }

    // process all layers and transform them into the final type
    Map<String, T> layers = new LinkedHashMap<>();
    for (Entry<String, String> entry : values.entrySet()) {
      T resolved = resolveValue(entry.getValue());
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

  @Override
  public String toString() {
    return format("Prop{%s=(%s)%s}", key, type.getSimpleName(), currentValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Prop)) {
      return false;
    }
    Prop<?> prop = (Prop<?>) o;
    return isRequired == prop.isRequired
        && isSecret == prop.isSecret
        && key.equals(prop.key)
        && type.equals(prop.type)
        && Objects.equals(defaultValue, prop.defaultValue)
        && Objects.equals(description, prop.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, type, defaultValue, description, isRequired, isSecret);
  }
}

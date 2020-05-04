package com.mihaibojin.prop;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.mihaibojin.resolvers.Resolver;
import java.util.Objects;
import java.util.Optional;

public abstract class Prop<T> {
  private final String key;
  private final Class<T> type;
  private final T defaultValue;
  private final String description;
  private final boolean isRequired;
  private final boolean isSecret;
  protected PropRegistry registry;
  protected String resolverId;

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
   * @return {@link String} representing a specific {@link Resolver} or an empty {@link Optional}
   *     when all registered resolvers should be queried.
   */
  protected Optional<String> resolverId() {
    return Optional.ofNullable(resolverId);
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
    synchronized (this) {
      String value = resolverId().map(id -> registry().get(key, id)).orElse(registry().get(key));
      T resolved = resolveValue(value);
      validate(resolved);

      if (Objects.equals(currentValue, resolved)) {
        return false;
      }

      currentValue = resolved;
    }

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
}

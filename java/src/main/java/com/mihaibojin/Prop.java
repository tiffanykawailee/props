package java.src.main.java.com.mihaibojin;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class Prop<T> {
  public final String key;
  public final Class<T> type;
  public final T defaultValue;
  public final String description;
  public final boolean isRequired;
  public final boolean isSecret;
  public final boolean isCacheable;
  public final boolean isDeprecated;

  public Prop(String key, Class<T> type, T defaultValue, String description,
      boolean isRequired,
      boolean isSecret,
      boolean isCacheable, boolean isDeprecated) {
    this.key = key;
    this.type = type;
    this.defaultValue = defaultValue;
    this.description = description;
    this.isRequired = isRequired;
    this.isSecret = isSecret;
    this.isCacheable = isCacheable;
    this.isDeprecated = isDeprecated;

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

    if (isRequired && isNull(defaultValue)) {
      throw new IllegalStateException(format("Prop '%s' is required, but a default value was not specified", key));
    }
  }

  @Override
  public String toString() {
    return format(
        "Prop(%s, type=%s, defaultValue=%s, required=%s)", key, type.getSimpleName(), defaultValue, isRequired);
  }
}

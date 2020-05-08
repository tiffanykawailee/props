package com.mihaibojin.prop;

import java.util.function.Function;

public class PropBuilder<T> {
  public final String key;
  private T defaultValue;
  private String description;
  private boolean isRequired;
  private boolean isSecret;

  private PropBuilder(String key) {
    this.key = key;
  }

  public static <T> PropBuilder<T> of(String key) {
    return new PropBuilder<>(key);
  }

  public PropBuilder defaultValue(T defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public PropBuilder description(String description) {
    this.description = description;
    return this;
  }

  public PropBuilder isRequired(boolean isRequired) {
    this.isRequired = isRequired;
    return this;
  }

  public PropBuilder isSecret(boolean isSecret) {
    this.isSecret = isSecret;
    return this;
  }

  public AbstractProp<T> build(Function<String, T> resolver) {
    return new AbstractProp<T>(key, defaultValue, description, isRequired, isSecret) {
      @Override
      public T decode(String value) {
        return resolver.apply(value);
      }
    };
  }
}

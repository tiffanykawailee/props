package com.mihaibojin.props.core;

public interface Prop<T> {
  String key();

  T defaultValue();

  String description();

  boolean isRequired();

  boolean isSecret();

  T value();
}

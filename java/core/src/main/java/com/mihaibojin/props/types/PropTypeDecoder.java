package com.mihaibojin.props.types;

import java.util.Objects;

public class PropTypeDecoder {
  /** Casts the passed input string to an Integer value */
  public static PropTypeConverter<Integer> asInteger() {
    return (value) -> Objects.requireNonNull(Utils.parseNumber(value)).intValue();
  }
}

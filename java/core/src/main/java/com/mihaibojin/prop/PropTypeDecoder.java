package com.mihaibojin.prop;

import com.mihaibojin.Utils;
import java.util.Objects;

public class PropTypeDecoder {
  /** Casts the passed input string to an Integer value */
  public static PropTypeConverter<Integer> asInteger() {
    return (value) -> Objects.requireNonNull(Utils.parseNumber(value)).intValue();
  }
}

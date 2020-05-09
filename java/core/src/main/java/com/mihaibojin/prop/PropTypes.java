package com.mihaibojin.prop;

import com.mihaibojin.Utils;
import java.util.Objects;

public class PropTypes {
  public static PropCoder<Integer> integer() {
    return (value) -> Objects.requireNonNull(Utils.parseNumber(value)).intValue();
  }
}
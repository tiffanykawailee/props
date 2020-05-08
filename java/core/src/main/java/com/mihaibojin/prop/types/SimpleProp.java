package com.mihaibojin.prop.types;

import com.mihaibojin.prop.AbstractProp;
import com.mihaibojin.resolvers.Resolver;

public abstract class SimpleProp<T> extends AbstractProp<T> {
  public SimpleProp(String key, T defaultValue) {
    super(key, defaultValue, null, false, false);
  }

  public SimpleProp(String key, T defaultValue, Resolver resolver) {
    super(key, defaultValue, null, false, false);
  }
}

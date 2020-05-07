package com.mihaibojin.prop.types;

import com.mihaibojin.prop.Prop;
import com.mihaibojin.resolvers.Resolver;

public abstract class SimpleProp<T> extends Prop<T> {
  public SimpleProp(String key, Class<T> type, T defaultValue) {
    super(key, type, defaultValue, null, false, false);
  }

  public SimpleProp(String key, Class<T> type) {
    super(key, type, null, null, true, false);
  }

  public SimpleProp(String key, Class<T> type, T defaultValue, Resolver resolver) {
    super(key, type, defaultValue, null, false, false);
  }
}

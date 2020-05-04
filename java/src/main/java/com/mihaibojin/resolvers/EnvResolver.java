package java.src.main.java.com.mihaibojin.resolvers;

import java.util.HashMap;
import java.util.Map;

public class EnvResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();
  private final boolean autoUpdate;

  public EnvResolver(boolean autoUpdate) {
    this.autoUpdate = autoUpdate;
  }

  @Override
  public boolean canAutoUpdate() {
    return autoUpdate;
  }

  @Override
  public void refresh() {
    store.putAll(System.getenv());
  }

  @Override
  public String get(String key) {
    return store.get(key);
  }
}

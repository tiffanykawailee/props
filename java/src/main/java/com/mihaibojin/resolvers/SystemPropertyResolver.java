package java.src.main.java.com.mihaibojin.resolvers;

import static java.src.main.java.com.mihaibojin.Utils.updateMapWithProperties;

import java.util.HashMap;
import java.util.Map;

public class SystemPropertyResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();
  private final boolean autoUpdate;

  public SystemPropertyResolver(boolean autoUpdate) {
    this.autoUpdate = autoUpdate;
  }

  @Override
  public String get(String key) {
    return store.get(key);
  }

  @Override
  public boolean canAutoUpdate() {
    return autoUpdate;
  }

  @Override
  public void refresh() {
    updateMapWithProperties(store, System.getProperties());
  }
}

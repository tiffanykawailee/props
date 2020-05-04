package java.src.main.java.com.mihaibojin.resolvers;

import static java.src.main.java.com.mihaibojin.Utils.updateMapWithProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileResolver implements Resolver {
  private final Map<String, String> store = new HashMap<>();
  private final Path configLocation;
  private final boolean autoUpdate;

  public PropertyFileResolver(Path configLocation, boolean autoUpdate) {
    this.configLocation = configLocation;
    this.autoUpdate = autoUpdate;
  }

  @Override
  public boolean canAutoUpdate() {
    return autoUpdate;
  }

  @Override
  public String get(String key) {
    return store.get(key);
  }

  @Override
  public void refresh() {
    if (!Files.exists(configLocation)) {
      // TODO: integrate with a logging provider
      return;
    }

    Properties properties = new Properties();

    try (InputStream stream = Files.newInputStream(configLocation)) {
      properties.load(stream);
    } catch (IOException e) {
      // TODO: integrate with a logging provider
    }

    updateMapWithProperties(store, properties);
  }
}
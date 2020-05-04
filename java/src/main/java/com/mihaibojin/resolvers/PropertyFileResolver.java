package com.mihaibojin.resolvers;

import com.mihaibojin.Utils;
import com.mihaibojin.logger.LoggerBridge;
import com.mihaibojin.logger.LoggerInterface;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyFileResolver implements Resolver {
  private static final LoggerInterface log = LoggerBridge.getLogger();

  private final Map<String, String> store = new HashMap<>();
  private final Path location;
  private final boolean autoUpdate;

  public PropertyFileResolver(Path location, boolean autoUpdate) {
    this.location = location;
    this.autoUpdate = autoUpdate;
  }

  @Override
  public boolean shouldAutoUpdate() {
    return autoUpdate;
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public void refresh() {
    if (!Files.exists(location)) {
      log.debug("Skipping {}; file not found at {}", getClass().getSimpleName(), location);
      return;
    }

    try (InputStream stream = Files.newInputStream(location)) {
      Utils.mergeMapsInPlace(store, Utils.loadPropertiesFromStream(stream));

    } catch (IOException e) {
      log.error("Could not read configuration from " + location, e);
    }
  }
}

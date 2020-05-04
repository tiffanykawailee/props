package com.mihaibojin.resolvers;

import static java.util.Objects.isNull;

import com.mihaibojin.Utils;
import com.mihaibojin.logger.LoggerBridge;
import com.mihaibojin.logger.LoggerInterface;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClasspathPropertyFileResolver implements Resolver {
  private static final LoggerInterface log = LoggerBridge.getLogger();

  private final Map<String, String> store = new HashMap<>();
  private final String location;
  private final boolean autoUpdate;

  public ClasspathPropertyFileResolver(String location, boolean autoUpdate) {
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
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream(location)) {
      if (isNull(stream)) {
        log.debug("Skipping {}; resource not found in classpath", location);
        return;
      }

      Utils.mergeMapsInPlace(store, Utils.loadPropertiesFromStream(stream));
    } catch (IOException e) {
      log.error("Could not read configuration from classpath: " + location, e);
    }
  }
}

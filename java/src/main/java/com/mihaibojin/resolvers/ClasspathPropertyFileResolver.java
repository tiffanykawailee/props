package com.mihaibojin.resolvers;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import com.mihaibojin.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasspathPropertyFileResolver implements Resolver {
  private static final Logger log = Logger.getLogger(ClasspathPropertyFileResolver.class.getName());

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
  public Set<String> refresh() {
    try (InputStream stream = getClass().getResourceAsStream(location)) {
      if (isNull(stream)) {
        log.fine(format("Skipping %s; resource not found in classpath", location));
        return Set.of();
      }

      return Utils.mergeMapsInPlace(store, Utils.loadPropertiesFromStream(stream));
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not read configuration from classpath: " + location, e);
    }
    return Set.of();
  }
}

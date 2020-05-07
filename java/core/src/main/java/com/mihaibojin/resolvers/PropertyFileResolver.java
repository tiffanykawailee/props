package com.mihaibojin.resolvers;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

import com.mihaibojin.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class PropertyFileResolver implements Resolver {
  private static final Logger log = Logger.getLogger(PropertyFileResolver.class.getName());

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
  public Set<String> refresh() {
    if (!Files.exists(location)) {
      log.fine(format("Skipping %s; file not found at %s", getClass().getSimpleName(), location));
      return Set.of();
    }

    try (InputStream stream = Files.newInputStream(location)) {
      return Utils.mergeMapsInPlace(store, Utils.loadPropertiesFromStream(stream));

    } catch (IOException e) {
      log.log(SEVERE, "Could not read configuration from " + location, e);
    }

    return Set.of();
  }
}

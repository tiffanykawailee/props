package com.mihaibojin.props.core.resolvers;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

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
  private final boolean isReloadable;

  /** Constructs a {@link Resolver} which should only read the properties file once */
  public PropertyFileResolver(Path location) {
    this(location, false);
  }

  public PropertyFileResolver(Path location, boolean isReloadable) {
    this.location = location;
    this.isReloadable = isReloadable;
  }

  @Override
  public boolean isReloadable() {
    return isReloadable;
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public Set<String> reload() {
    if (!Files.exists(location)) {
      log.fine(format("Skipping %s; file not found at %s", getClass().getSimpleName(), location));
      return Set.of();
    }

    try (InputStream stream = Files.newInputStream(location)) {
      return ResolverUtils.mergeMapsInPlace(store, ResolverUtils.loadPropertiesFromStream(stream));

    } catch (IOException e) {
      log.log(SEVERE, "Could not read configuration from " + location, e);
    }

    return Set.of();
  }
}

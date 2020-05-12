package com.mihaibojin.props.core.resolvers;

import static com.mihaibojin.props.core.resolvers.ResolverUtils.formatResolverId;
import static java.lang.String.format;
import static java.util.Objects.isNull;

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
  private final boolean isReloadable;

  /** Constructs a {@link Resolver} which should only read properties from the classpath once */
  public ClasspathPropertyFileResolver(String location) {
    this(location, false);
  }

  public ClasspathPropertyFileResolver(String location, boolean isReloadable) {
    this.location = location;
    this.isReloadable = isReloadable;
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(store.get(key));
  }

  @Override
  public Set<String> reload() {
    try (InputStream stream = getClass().getResourceAsStream(location)) {
      if (isNull(stream)) {
        log.fine(format("Skipping %s; resource not found in classpath", location));
        return Set.of();
      }

      return ResolverUtils.mergeMapsInPlace(store, ResolverUtils.loadPropertiesFromStream(stream));
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not read configuration from classpath: " + location, e);
    }

    return Set.of();
  }

  @Override
  public boolean isReloadable() {
    return isReloadable;
  }

  @Override
  public String defaultId() {
    return formatResolverId(location);
  }
}

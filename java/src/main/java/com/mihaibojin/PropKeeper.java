package java.src.main.java.com.mihaibojin;

import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

import java.nio.file.Paths;
import java.src.main.java.com.mihaibojin.resolvers.EnvResolver;
import java.src.main.java.com.mihaibojin.resolvers.PropertyFileResolver;
import java.src.main.java.com.mihaibojin.resolvers.Resolver;
import java.src.main.java.com.mihaibojin.resolvers.SystemPropertyResolver;
import java.util.List;
import java.util.ListIterator;

public class PropKeeper {

  public PropKeeper() {
    List<Resolver> resolvers = List.of(
        new SystemPropertyResolver(true),
        new EnvResolver(true),
        new PropertyFileResolver(Paths.get("config.properties"), true)
    );
  }

  public void update(List<Resolver> resolvers) {
    resolvers.parallelStream()
        .filter(not(Resolver::canAutoUpdate))
        .forEach(Resolver::refresh);
  }

  public String getProp(List<Resolver> resolvers, String key) {
    ListIterator<Resolver> it = resolvers.listIterator(resolvers.size());
    while (it.hasPrevious()) {
      String value = it.previous().get(key);
      if (nonNull(value)) {
        return value;
      }
    }

    return null;
  }
}

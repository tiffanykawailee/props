package com.mihaibojin;

import com.mihaibojin.prop.PropRegistry;
import com.mihaibojin.resolvers.EnvResolver;
import com.mihaibojin.resolvers.PropertyFileResolver;
import com.mihaibojin.resolvers.SystemPropertyResolver;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class PropRegistryTest {
  @Test
  public void testRegistry() {
    PropRegistry registry =
        new PropRegistry.Builder()
            .withResolver("SYSTEM", new SystemPropertyResolver())
            .withResolver("ENV", new EnvResolver())
            .withResolver("CONFIG", new PropertyFileResolver(Paths.get("config.properties"), true))
            .build();
  }
}

package com.mihaibojin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.mihaibojin.prop.PropRegistry;
import com.mihaibojin.prop.PropRegistry.Builder;
import com.mihaibojin.prop.types.SimpleProp;
import com.mihaibojin.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.resolvers.EnvResolver;
import com.mihaibojin.resolvers.SystemPropertyResolver;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class PropRegistryTest {
  @Test
  public void registrySmokeTest() {
    // ARRANGE
    PropRegistry registry =
        new Builder()
            .withResolver("SYSTEM", new SystemPropertyResolver())
            .withResolver("ENV", new EnvResolver())
            .withResolver(
                "CONFIG", new ClasspathPropertyFileResolver("/propfiles/config.properties", true))
            .build();

    SimpleProp<Integer> aProp =
        new SimpleProp<>("test.prop", Integer.class, null) {
          @Override
          public Integer resolveValue(String value) {
            return Objects.requireNonNull(Utils.parseNumber(value)).intValue();
          }
        };

    registry.bind(aProp);

    assertThat(aProp.value(), equalTo(1));
  }
}

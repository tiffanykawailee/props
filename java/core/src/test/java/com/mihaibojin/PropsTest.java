package com.mihaibojin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.mihaibojin.prop.Prop;
import com.mihaibojin.prop.PropTypes;
import com.mihaibojin.prop.Props;
import com.mihaibojin.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.resolvers.EnvResolver;
import com.mihaibojin.resolvers.SystemPropertyResolver;
import org.junit.jupiter.api.Test;

public class PropsTest {
  @Test
  public void registrySmokeTest() {
    // ARRANGE
    Props props =
        Props.factory()
            .withResolver("SYSTEM", new SystemPropertyResolver())
            .withResolver("ENV", new EnvResolver())
            .withResolver(
                "CONFIG", new ClasspathPropertyFileResolver("/propfiles/config.properties"))
            .build();

    Prop<Integer> aProp = props.create("test.prop", PropTypes.integer());

    assertThat(aProp.value(), equalTo(1));
  }
}

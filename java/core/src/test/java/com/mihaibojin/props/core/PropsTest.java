package com.mihaibojin.props.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.mihaibojin.props.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.props.resolvers.EnvResolver;
import com.mihaibojin.props.resolvers.SystemPropertyResolver;
import com.mihaibojin.props.types.PropTypeDecoder;
import org.junit.jupiter.api.Test;

public class PropsTest {
  @Test
  public void loadValueFromClassPath() {
    // ARRANGE
    Props props =
        Props.factory()
            .withResolver("SYSTEM", new SystemPropertyResolver())
            .withResolver("ENV", new EnvResolver())
            .withResolver(
                "CONFIG", new ClasspathPropertyFileResolver("/propfiles/config1.properties"))
            .build();

    // ACT
    Integer aValue = props.create("prop.id", PropTypeDecoder.asInteger()).build().value();

    // ASSERT
    assertThat(aValue, equalTo(1));
  }

  @Test
  public void resolverPriorityOrder() {
    // ARRANGE
    Props props =
        Props.factory()
            .withResolver(
                "CONFIG1", new ClasspathPropertyFileResolver("/propfiles/config1.properties"))
            .withResolver(
                "CONFIG2", new ClasspathPropertyFileResolver("/propfiles/config2.properties"))
            .build();

    // ACT
    Integer aValue = props.create("prop.id", PropTypeDecoder.asInteger()).build().value();

    // ASSERT
    assertThat(aValue, equalTo(2));
  }

  @Test
  public void getValueFromSpecificResolver() {
    // ARRANGE
    Props props =
        Props.factory()
            .withResolver(
                "CONFIG1", new ClasspathPropertyFileResolver("/propfiles/config1.properties"))
            .withResolver(
                "CONFIG2", new ClasspathPropertyFileResolver("/propfiles/config2.properties"))
            .build();

    // ACT
    Integer aValue =
        props.create("prop.id", PropTypeDecoder.asInteger()).resolver("CONFIG1").build().value();

    // ASSERT
    assertThat(aValue, equalTo(1));
  }
}

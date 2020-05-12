package com.mihaibojin.props.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.mihaibojin.props.core.converters.IntegerConverter;
import com.mihaibojin.props.core.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.props.core.resolvers.EnvResolver;
import com.mihaibojin.props.core.resolvers.SystemPropertyResolver;
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
    Integer aValue = props.prop("prop.id", new IntegerConverter() {}).build().value();

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
    Integer aValue = props.prop("prop.id", new IntegerConverter() {}).build().value();

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
        props.prop("prop.id", new IntegerConverter() {}).resolver("CONFIG1").build().value();

    // ASSERT
    assertThat(aValue, equalTo(1));
  }
}

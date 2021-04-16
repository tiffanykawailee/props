/*
 * Copyright 2020 Mihai Bojin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import com.mihaibojin.props.core.Prop;
import com.mihaibojin.props.core.Props;
import com.mihaibojin.props.core.ValidationException;
import com.mihaibojin.props.core.converters.Cast;
import com.mihaibojin.props.core.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.props.core.types.AbstractStringProp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PropMetadataExamplesTest {

  private Props props;

  @BeforeEach
  void setUp() {
    // initialize the Props registry
    props =
        Props.factory()
            .withResolver(new ClasspathPropertyFileResolver("/examples/prop_metadata.properties"))
            .build();
  }

  @Test
  void requiredPropMustHaveAValueOrADefault() {
    props.bind(new RequiredProp("undefined.prop", null));
    RequiredProp aProp = props.retrieve("undefined.prop");

    Assertions.assertThrows(
        ValidationException.class,
        aProp::value,
        "Expecting prop to throw, since it is required but is missing a value");
  }

  @Test
  void requiredUnBoundPropMustHaveAValue() {
    Assertions.assertThrows(
        ValidationException.class,
        () -> props.prop("undefined.prop").isRequired(true).value(),
        "Expecting unbound props to throw, if they don't have values or defaults");
  }

  @Test
  void requiredPropReturnsTheDefault() {
    // bind a prop for which we do not define a value, but define a default
    props.bind(new RequiredProp("undefined.prop", "DEFAULT"));
    RequiredProp aProp = props.retrieve("undefined.prop");

    assertThat("Expecting the default value to be returned", aProp.value(), equalTo("DEFAULT"));
  }

  @Test
  void readDefaultOnlyOnce() {
    // attempt to read a prop's value or return the defined default
    String maybeValue = props.prop("undefined.prop").defaultValue("DEFAULT").value();

    assertThat("Expecting the default value to be returned", maybeValue, equalTo("DEFAULT"));
  }

  @Test
  void readDefaultValue() {
    // bind a prop for which we do not define a value, but define a default
    Prop<String> aProp = props.prop("undefined.prop").defaultValue("DEFAULT").build();

    assertThat("Expecting the default value to be returned", aProp.value(), equalTo("DEFAULT"));
  }

  @Test
  void readDefaultValueWithType() {
    // bind a prop for which we do not define a value, but define a default
    Prop<Integer> aProp = props.prop("int.prop", Cast.asInteger()).defaultValue(1).build();

    assertThat("Expecting the default value to be returned", aProp.value(), equalTo(1));
  }

  @Test
  void secretPropDoesNotPrintItsValue() {
    // bind a prop for which we do not define a value, but define a default
    props.bind(new SecretProp("secret.prop"));
    SecretProp aProp = props.retrieve("secret.prop");

    assertThat(
        "Expecting the actual value to not be printed",
        aProp.toString(),
        not(containsString("BIG-SECRET")));
    assertThat(
        "Expecting a redacted value",
        aProp.toString(),
        containsString(aProp.redact(aProp.value())));
  }

  private static class RequiredProp extends AbstractStringProp {

    RequiredProp(String key, String defaultValue) {
      super(key, defaultValue, null, true, false);
    }
  }

  private static class SecretProp extends AbstractStringProp {

    SecretProp(String key) {
      super(key, null, null, true, true);
    }

    @Override
    public String redact(String value) {
      return super.redact(value);
    }
  }
}

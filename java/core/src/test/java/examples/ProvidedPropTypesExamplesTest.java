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

import com.mihaibojin.props.core.Props;
import com.mihaibojin.props.core.resolvers.ClasspathPropertyFileResolver;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ProvidedPropTypesExamplesTest {
  private Props props;

  @BeforeEach
  void setUp() {
    // initialize the Props registry
    props =
        Props.factory()
            .withResolver(
                new ClasspathPropertyFileResolver("/examples/provided_prop_types.properties"))
            .build();
  }

  // TODO(mihaibojin): add examples for each supported type

  @Test
  @Disabled
  void readTypes() {
    // initialize a String prop and read its value once
    Optional<String> maybeValue = props.prop("a.TBD").readOnce();

    // assert that the value is retrieved
    assertThat(
        "Expected to read and correctly cast the property", maybeValue.get(), equalTo("TBD"));
  }
}

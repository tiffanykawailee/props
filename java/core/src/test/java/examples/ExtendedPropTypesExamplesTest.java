package examples;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.mihaibojin.props.core.Prop;
import com.mihaibojin.props.core.Props;
import com.mihaibojin.props.core.resolvers.ClasspathPropertyFileResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ExtendedPropTypesExamplesTest {
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

  // TODO(mihaibojin): prop updates, encoder/decoder, custom validation + validation failure

  @Test
  @Disabled
  void readTypes() {
    // initialize a prop
    Prop<String> aProp = props.prop("a.TBD").build();

    // assert that the value is retrieved
    assertThat("Expected to read and correctly cast the property", aProp.value(), equalTo("TBD"));
  }
}

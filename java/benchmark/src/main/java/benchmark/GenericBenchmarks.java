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

package benchmark;

import static java.lang.String.format;

import com.mihaibojin.props.core.Props;
import com.mihaibojin.props.core.converters.StringListConverter;
import com.mihaibojin.props.core.resolvers.ClasspathPropertyFileResolver;
import com.mihaibojin.props.core.resolvers.EnvResolver;
import com.mihaibojin.props.core.resolvers.SystemPropertyResolver;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

public class GenericBenchmarks {

  // @Setup

  /** Initialize the {@link Props} registry */
  @State(Scope.Benchmark)
  public static class PropsState {
    final Props props =
        Props.factory()
            .withResolver(new SystemPropertyResolver())
            .withResolver(new EnvResolver())
            .withResolver(new ClasspathPropertyFileResolver("/benchmark.properties"))
            .build();
  }

  /** Loads a String {@link com.mihaibojin.props.core.Prop}. */
  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.SECONDS)
  @Fork(value = 1, warmups = 1)
  public void loadStringProp(PropsState state, Blackhole blackhole) {
    try (state.props) {
      Optional<String> value = state.props.prop("a.string").readOnce();
      if (value.isEmpty() || !Objects.equals(value.get(), "one")) {
        throw new AssertionError(format("Expected %s, got %s", "one", value.orElse(null)));
      }
    }
  }

  /** Loads a String {@link com.mihaibojin.props.core.Prop}. */
  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.SECONDS)
  @Fork(value = 1, warmups = 1)
  public void loadListStringProp(PropsState state, Blackhole blackhole) {
    try (state.props) {
      Optional<List<String>> value =
          state.props.prop("a.string.list", new StringListConverter() {}).readOnce();
      if (value.isEmpty() || !Objects.equals(value.get(), List.of("one", "two", "three"))) {
        throw new AssertionError(
            format("Expected %s, got %s", "one,two,three", value.orElse(null)));
      }
    }
  }
}

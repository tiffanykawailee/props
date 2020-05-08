- add Change events to Prop.java and allow users to register to them 

- tests

- custom validation + validation failure

- ability to mock the response from a specific layer

- access to set override values for props directly in the registry (for testing) (thread-safe)

Types of props:
- Prop implementation / class (fully spec'd Prop w. dedicated class object, needs a better name)
- SimpleProp (minimal config required to retrieve values and get updates)
- TBD

- https://github.com/bazelbuild/bazel/issues/3410#issuecomment-398125365
```
bazel build \
  --java_toolchain=@bazel_tools//tools/jdk:toolchain_java9 \
  --host_java_toolchain=@bazel_tools//tools/jdk:toolchain_java9 ...
```

Useful links:
https://medium.com/wix-engineering/migrating-to-bazel-from-maven-or-gradle-part-1-how-to-choose-the-right-build-unit-granularity-a58a8142c549
https://github.com/bazelbuild/bazel/issues/6681

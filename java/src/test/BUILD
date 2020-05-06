load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("@rules_jvm_external//:defs.bzl", "artifact")

## https://docs.bazel.build/versions/master/test-encyclopedia.html
## https://docs.bazel.build/versions/master/be/java.html#java_test

# For more information, see https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-bazel
load("//java:junit5.bzl", "java_junit5_test")

java_junit5_test(
    name = "junit5-test",
    srcs = [
        "java/com/mihaibojin/PropRegistryTest.java"
    ],
    test_package = "com.mihaibojin",
    deps = [
        "//java/src/main:props",
    ],
)

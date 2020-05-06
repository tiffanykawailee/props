load("@rules_jvm_external//:defs.bzl", "artifact")

## https://docs.bazel.build/versions/master/test-encyclopedia.html
## https://docs.bazel.build/versions/master/be/java.html#java_test

# For more information, see https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-bazel
load("//java:junit5.bzl", "java_junit5_test")

java_library(
    name = "hamcrest",
    testonly = True,
    exports = [
        artifact("org.hamcrest:hamcrest-library"),
        artifact("org.hamcrest:hamcrest-core"),
        artifact("org.hamcrest:hamcrest"),
    ],
)

java_library(
    name = "mockito",
    testonly = True,
    exports = [
        artifact("org.mockito:mockito-core"),
    ],
)

java_junit5_test(
    name = "AllTests",
    srcs = glob(["**/*Test.java"]),
    resources = glob(["resources/**"]),
    test_package = "com.mihaibojin",
    deps = [
        "//java/src/main:props",
        ":hamcrest",
        ":mockito",
    ],
)

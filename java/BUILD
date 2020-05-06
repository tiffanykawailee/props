load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")
load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "props",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    visibility = ["//src/test:__subpackages__"],
)

# https://docs.bazel.build/versions/master/test-encyclopedia.html
# https://docs.bazel.build/versions/master/be/java.html#java_test
java_test(
    name = "tests",
    srcs = glob(["src/test/java/**/*Test.java"]),
    resources = glob(["src/test/resources/**"]),
    size = "small",
    deps = [
        ":props",
        artifact("org.testng:testng"),
    ],
)
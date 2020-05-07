load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")
load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "props",
    srcs = glob(["java/**/*.java"]),
    resources = glob(["resources/**"]),
    visibility = ["//java/core/src/test:__subpackages__"],
)

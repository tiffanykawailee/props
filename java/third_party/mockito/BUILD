load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "mockito",
    testonly = True,
    exports = [
        artifact("org.mockito:mockito-core"),
    ],
    visibility = ["//java:__subpackages__"],
)

load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "hamcrest",
    testonly = True,
    exports = [
        artifact("org.hamcrest:hamcrest-library"),
        artifact("org.hamcrest:hamcrest-core"),
        artifact("org.hamcrest:hamcrest"),
    ],
    visibility = ["//java:__subpackages__"],
)

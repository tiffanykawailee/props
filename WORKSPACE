# BEGIN https://github.com/bazelbuild/rules_jvm_external
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "3.2"
RULES_JVM_EXTERNAL_SHA = "82262ff4223c5fda6fb7ff8bd63db8131b51b413d26eb49e3131037e79e324af"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
# END https://github.com/bazelbuild/rules_jvm_external

# BEGIN java dependencies
load("@rules_jvm_external//:specs.bzl", "maven")
maven_install(
    fetch_sources = True,
    artifacts = [
        maven.artifact(
            group = "com.google.code.findbugs",
            artifact = "jsr305",
            version = "3.0.2",
            neverlink = True,
        ),
        maven.artifact(
            group = "com.google.errorprone",
            artifact = "error_prone_annotations",
            version = "2.3.4",
            neverlink = True,
        ),
        maven.artifact(
            group = "org.testng",
            artifact = "testng",
            version = "7.1.0",
            testonly = True,
        ),
        maven.artifact(
            group = "org.hamcrest",
            artifact = "hamcrest-library",
            version = "1.3",
            testonly = True,
        ),
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)
# END java dependencies

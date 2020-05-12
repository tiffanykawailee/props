load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")
load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "props-core",
    srcs = glob(["java/**/*.java"]),
    resources = glob(["resources/**"]),
    tags = ["maven_coordinates=com.mihaibojin.props:props-core:{pom_version}"],
    visibility = ["//java/core/src/test:__subpackages__"],
)

load("@graknlabs_bazel_distribution//maven/templates:rules.bzl", "assemble_maven", "deploy_maven")
assemble_maven(
    name = "assemble-maven",
    target = "//java/core/src/main:props-core",
    package = "{maven_packages}",
    project_name = "Props",
    project_description = "Props - Multi-layered properties and secrets management library",
    project_url = "https://github.com/MihaiBojin/props",
    scm_url = "https://github.com/MihaiBojin/props.git",
    version_file = "//java/central-sync:VERSION",
    developers = {"1": ["name=Mihai Bojin", "email=mihai.bojin@gmail.com"]},
    license = "apache"
)
deploy_maven(
    name = "deploy-maven",
    target = ":assemble-maven",
    deployment_properties = "//java/central-sync:deployment.properties"
)

load("//java/third_party/javadoc:javadoc.bzl", "javadoc")
javadoc(
    name = "props-doc",
    srcs = glob(["java/**/*.java"]),
    group_id = "com.mihaibojin.props",
    artifact_id = "props-core",
)
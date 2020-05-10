### Backlog

- example with encode/decode

- configure JPMS/exports: https://www.baeldung.com/java-9-modularity

- prepare for publishing to Maven Central
  - https://medium.com/wix-engineering/how-to-publish-artifacts-from-bazel-to-maven-central-71da0cf990f5
  - create a GPG key and upload it to GitHub (along with `DEPLOY_MAVEN_USERNAME` and `DEPLOY_MAVEN_PASSWORD`)
  - configure `deploy_maven`
  - create a Developers map under `contributors/contributors.json|yml` and load it in Starlark; update the CONTRIBUTING guide
    ```
    developers = {"1": ["name=Mihai Bojin", "email=m@g.m", "organization=null"]
    ```
  - create a GitHub action that runs `bazel run //path/to/artifact:deploy-maven -- release --gpg` on tag push
  - read the v-tag and output the numeric part to a `VERSION` file

- tests

- custom validation + validation failure

- add Change events to Prop.java and allow users to register to them 

- Props handles all loading, lazy loading, prop->layer association

- ability to mock the response from a specific layer


### Useful links
- https://medium.com/wix-engineering/migrating-to-bazel-from-maven-or-gradle-part-1-how-to-choose-the-right-build-unit-granularity-a58a8142c549
- https://github.com/bazelbuild/bazel/issues/6681

# Contributing to props

Thanks for your interest in contributing to `props`! 


# Developer set-up


## Prerequisites and Tools 

- [Git](https://git-scm.com/) (usually provided by the OS)
- [Jabba](https://github.com/shyiko/jabba) for Java SDK management
- [Bazel](https://bazel.build/) (we prefer [Bazelisk](https://github.com/bazelbuild/bazelisk))

You can run `make setup` to install *Jabba* and *Bazelisk*.  This target also sets up git hooks,
which runs formatters and static code analysis tools.

Also, you will need a text editor or IDE; we use [IntelliJ](https://www.jetbrains.com/idea/).


## Get the code

- Fork the [repository](https://github.com/MihaiBojin/props)
- Clone your forked repository locally (`git clone https://github.com/[USERNAME]/props`)
- Add this repo as an upstream: `git remote add upstream https://github.com/MihaiBojin/props`


## Building and testing

Before doing anything else, run `jabba use` in the project's root directory.
This will ensure you are running the version of the JDK used (and tested) for this project.

You can build the project with: `make build`.

And you can run the tests with `make test`.

We use the [Google JAVA Formatter](https://github.com/google/google-java-format) to keep the code 
consistent.  You can use `make fmt` to format the code and `make fmtcheck` to verify that 
all code is properly formatted.  This target runs in the pre-commit git hook which is automatically 
installed when you run `make setup` or can be individually installed with `make git-hooks`. 


# Contributing

- Please [create a GitHub issue](https://github.com/MihaiBojin/props/issues/new)
- Then open a Pull Request and tag the newly created issue (e.g., `Fixes #12345`)
- For PRs to be accepted, you must agree to the terms of the [CLA](CLA.md) and to 
  contribute the code under the project's [license](./LICENSE).

To sign the [Contributor License Agreement](./CLA.md) please follow the 
[steps outlined here](./contributors/README.md). 

**Reviewers will ensure that all requirements are met before merging PRs.**


# License

This software is distributed without any warranty and under the terms of the
[Apache License, Version 2.0](./LICENSE), included with the project.

Copyright 2020 Mihai Bojin

Please also review the project's [contributor license agreement](CLA.md).

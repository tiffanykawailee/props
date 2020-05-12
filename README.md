# props

This project aims to provide a library which manages properties and secrets coming from
multiple sources (startup properties, environment, property files, etc.)

While `props` is currently implemented in JAVA, additional languages are planned in the future.


# Features

- Simple and straightforward API
- Optimized for reads, property values are cached and updated when changed
- No third-party dependencies in the core module
- Periodically refresh properties from sources
- Thread-safety
- Ability to specify the order in which sources are queried for values


# Contributing to the project

Please see the [contributor guide](./CONTRIBUTING.md).


# License

This software is distributed without any warranty and under the terms of the
[Apache License, Version 2.0](./LICENSE), included with the project.

Copyright 2020 Mihai Bojin

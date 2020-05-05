# props

This project aims to provide a library which manages properties and secrets coming from
multiple sources (startup properties, environment, property files, etc.)

The initial implementation will be in JAVA; future implementation in other languages are planned.

** The project is currently considering Work In Progress and not yet ready for production! **

# Vision

- Simple and straightforward API
- Fast read access times
- No third-party dependencies in the core module

# Features

- Periodically refresh properties from sources
- Thread-safe
- Ability to configure the order in which properties are resolved from sources
- Ability to register to change events
- Ability to extend the project (custom sources and typed property classes)

# Contributing to the project

Please see the [contributor guide](./CONTRIBUTING.md).

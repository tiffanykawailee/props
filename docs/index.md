---
---

# props

This project aims to provide a library which manages properties and secrets coming from
multiple sources (startup properties, environment, property files, etc.)

While `props` is currently implemented in JAVA, additional languages are planned in the future.

![](https://github.com/MihaiBojin/props/workflows/Deploy%20to%20Maven%20Central/badge.svg)

# Features

- Simple and straightforward API
- Optimized for reads, property values are cached and updated when changed
- No third-party dependencies in the core module
- Periodically refresh properties from sources
- Thread-safety
- Ability to specify the order in which sources are queried for values


# Quickstart

Add `props` as a dependency in your project.

```xml
<dependency>
  <groupId>com.mihaibojin.props</groupId>
  <artifactId>props-core</artifactId>
  <version>0.0.1</version>
</dependency>
```

Initialize a registry object which resolves values from the `system properties` and the `environment`.

```java
Props props = Props.factory()
    .withResolver(new SystemPropertyResolver())
    .withResolver(new EnvResolver())
    .build();
```

Create and bind a `String` property which retrieves the value of `file.encoding` 
from the system properties.

```java
Prop<String> aProp = props.prop("file.encoding").build();

aProp.value(); // will return an encoding such as UTF-8
```

Read more to see other features.


# The Props registry

To use the library, you first need to initialize a 
[Props](https://github.com/MihaiBojin/props/blob/master/java/core/src/main/java/com/mihaibojin/props/core/Props.java) 
registry. 

```java
Props props = Props.factory()
    .withResolver(...)
    .build();
```

Once configured, the `Props` object can:
- create new property (`Prop`) objects
- register (bind) property objects created by the user
- periodically refresh the values of all registered objects

All the examples below assume you have initialized a `Props` registry.


## Resolvers

`Props` has a number of predefined [resolvers](https://github.com/MihaiBojin/props/tree/master/java/core/src/main/java/com/mihaibojin/props/core/resolvers).
Support for the following sources is provided out of the box:
- System properties
- The environment
- JAVA property files on the classpath
- JAVA property files on disk

Additionally, you may define custom resolvers by implementing the 
[Resolver](https://github.com/MihaiBojin/props/blob/master/java/core/src/main/java/com/mihaibojin/props/core/resolvers/Resolver.java) 
interface.

The following examples assume you have already configured a `Props` registry.


## Type-safe properties

`Props` supports multiple [types](https://github.com/MihaiBojin/props/tree/master/java/core/src/main/java/com/mihaibojin/props/core/types) out of the box.

For example, you can load an `integer` property's value, without registering a prop with the registry:

```java
Optional<Integer> maybeValue = props.prop("prop.key", new IntegerConverter() {}).readOnce();
```

Or if you need to retrieve its value more than once, you can `bind` it to the registry as follows: 

```java
Prop<Integer> aProp = props.prop("prop.key", new IntegerConverter() {}).build();
aProp.value(); // will return the current value, at calling time
```


## Resolver precedence

If more than one resolver has a value for `prop.key`, the last one to define it 
will decide the property's value.

```java
Props props =
    Props.factory()
        .withResolver(new PropertyFileResolver(...)) // prop.key="one"
        .withResolver(new PropertyFileResolver(...)) // prop.key="two"
        .build();

props.prop("prop.key").readOnce(); // will return "two"
```


## Retrieving values from a specific resolver

Properties can be registered with a single resolver.  In that case, the registry will disregard 
all but the configured resolver.

```java
Props props =
    Props.factory()
        .withResolver("MY-ID", new PropertyFileResolver(...)) // prop.key="one"
        .withResolver(new PropertyFileResolver(...)) // prop.key="two"
        .build();

props.prop("prop.key").resolver("MY-ID").readOnce(); // will return "one"
```


## No value found

If no value can be found for the specified property, `Prop.value()` will return `null`.
```java
props.prop("unknown.prop").readOnce(); // will return null
```

## Further examples

Further examples are included as part of the 
[examples test suite](https://github.com/MihaiBojin/props/tree/master/java/core/src/test/java/examples).

These examples include code samples for the following features:
- defining props with required values
- specifying default values
- marking props as secrets
- defining custom prop classes
- defining custom value encoders
- extending prop validation
- retrieving prop values after a resolver update


# Contributing to the project

Please see the [contributor guide](https://github.com/MihaiBojin/props/blob/master/CONTRIBUTING.md).


# License

Copyright 2020 Mihai Bojin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

/*
 * Copyright 2020 Mihai Bojin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mihaibojin.props.core;

import com.mihaibojin.props.core.converters.PropTypeConverter;

public interface Prop<T> extends PropTypeConverter<T> {

  /** Identifies the {@link Prop} */
  String key();

  /**
   * @return a default value of type <code>T</code>, or null of the {@link Prop} does not have a
   *     default value
   */
  T defaultValue();

  /** @return a short description explaining what this prop is used for; developer-friendly */
  String description();

  /**
   * @return <code>true</code> if this {@link Prop} requires a value (validation will fail if one is
   *     not specified)
   */
  boolean isRequired();

  /**
   * @return <code>true</code> if this {@link Prop} represents a secret and its value should be
   *     redacted when printed
   */
  boolean isSecret();

  /** @return the {@link Prop}'s current value, or <code>null</code> if one was not set */
  T value();
}

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

package com.mihaibojin.props.core.types;

import com.mihaibojin.props.core.AbstractProp;
import com.mihaibojin.props.core.Prop;
import com.mihaibojin.props.core.converters.InstantConverter;
import java.time.Instant;

/**
 * Helper class meant to act as a base class when definining a {@link Prop} with the underlying
 * type.
 */
public abstract class AbstractInstantProp extends AbstractProp<Instant>
    implements InstantConverter {
  protected AbstractInstantProp(
      String key, Instant defaultValue, String description, boolean isRequired, boolean isSecret) {
    super(key, defaultValue, description, isRequired, isSecret);
  }
}

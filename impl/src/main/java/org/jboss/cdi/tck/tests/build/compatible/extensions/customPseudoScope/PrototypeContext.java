/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class PrototypeContext implements AlterableContext {
    public Class<? extends Annotation> getScope() {
        return Prototype.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        return creationalContext != null ? contextual.create(creationalContext) : null;
    }

    public <T> T get(Contextual<T> contextual) {
        return null;
    }

    public boolean isActive() {
        return true;
    }

    public void destroy(Contextual<?> contextual) {
    }
}

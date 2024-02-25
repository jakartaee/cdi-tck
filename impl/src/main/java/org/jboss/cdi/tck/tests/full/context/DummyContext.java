/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class DummyContext implements Context {
    public <T> T get(Contextual<T> bean, CreationalContext<T> creationalContext) {
        throw new UnsupportedOperationException();
    }

    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    public Class<? extends Annotation> getScope() {
        return DummyScoped.class;
    }

    public boolean isActive() {
        return true;
    }
}
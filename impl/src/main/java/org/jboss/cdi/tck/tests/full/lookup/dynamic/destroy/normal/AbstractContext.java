/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.lookup.dynamic.destroy.normal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public abstract class AbstractContext implements Context {

    protected static class Instance {
        private final Object instance;
        private final CreationalContext<?> ctx;

        public Instance(Object instance, CreationalContext<?> ctx) {
            this.instance = instance;
            this.ctx = ctx;
        }

        public Object getInstance() {
            return instance;
        }

        public CreationalContext<?> getCtx() {
            return ctx;
        }
    }

    protected final Map<Contextual<?>, Instance> storage = new ConcurrentHashMap<Contextual<?>, Instance>();

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        T instance = get(contextual);
        if (instance == null) {
            storage.put(contextual, new Instance(contextual.create(creationalContext), creationalContext));
            instance = get(contextual);
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Contextual<T> contextual) {
        Instance instance = storage.get(contextual);
        if (instance != null) {
            return (T) instance.instance;
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }

}

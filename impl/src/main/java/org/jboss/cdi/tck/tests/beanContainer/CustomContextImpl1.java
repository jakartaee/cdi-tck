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
package org.jboss.cdi.tck.tests.beanContainer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

// dummy context, always active
public class CustomContextImpl1 implements AlterableContext {
    private final ThreadLocal<Map<Contextual<?>, ContextualInstance<?>>> current = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Class<? extends Annotation> getScope() {
        return CustomScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Map<Contextual<?>, ContextualInstance<?>> store = current.get();
        ContextualInstance<T> instance = (ContextualInstance<T>) store.get(contextual);
        if (instance == null && creationalContext != null) {
            instance = new ContextualInstance<T>(contextual.create(creationalContext), creationalContext, contextual);
            store.put(contextual, instance);
        }
        return instance != null ? instance.get() : null;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void destroy(Contextual<?> contextual) {
        Map<Contextual<?>, ContextualInstance<?>> store = current.get();
        ContextualInstance<?> contextualInstance = store.remove(contextual);
        if (contextualInstance != null) {
            contextualInstance.destroy();
        }
    }

    static final class ContextualInstance<T> {
        private final T value;
        private final CreationalContext<T> creationalContext;
        private final Contextual<T> contextual;

        ContextualInstance(T instance, CreationalContext<T> creationalContext, Contextual<T> contextual) {
            this.value = instance;
            this.creationalContext = creationalContext;
            this.contextual = contextual;
        }

        T get() {
            return value;
        }

        void destroy() {
            contextual.destroy(value, creationalContext);
        }
    }
}

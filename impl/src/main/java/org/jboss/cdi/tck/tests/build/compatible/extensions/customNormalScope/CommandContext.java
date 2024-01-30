/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CommandContext implements AlterableContext {
    private final ThreadLocal<Map<Contextual<?>, ContextualInstance<?>>> currentContext = new ThreadLocal<>();
    private final ThreadLocal<CommandExecution> currentCommandExecution = new ThreadLocal<>();

    public Class<? extends Annotation> getScope() {
        return CommandScoped.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Map<Contextual<?>, ContextualInstance<?>> store = currentContext.get();

        if (store == null) {
            throw new ContextNotActiveException();
        }

        ContextualInstance<T> instance = (ContextualInstance<T>) store.get(contextual);
        if (instance == null && creationalContext != null) {
            instance = new ContextualInstance<T>(contextual.create(creationalContext), creationalContext, contextual);
            store.put(contextual, instance);
        }
        return instance != null ? instance.get() : null;
    }

    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    public boolean isActive() {
        return currentContext.get() != null;
    }

    public void destroy(Contextual<?> contextual) {
        Map<Contextual<?>, ContextualInstance<?>> ctx = currentContext.get();
        if (ctx == null) {
            return;
        }
        ContextualInstance<?> contextualInstance = ctx.remove(contextual);
        if (contextualInstance != null) {
            contextualInstance.destroy();
        }
    }

    void activate() {
        currentContext.set(new HashMap<>());
        currentCommandExecution.set(new CommandExecution());
    }

    void deactivate() {
        Map<Contextual<?>, ContextualInstance<?>> ctx = currentContext.get();
        if (ctx == null) {
            return;
        }
        for (ContextualInstance<?> instance : ctx.values()) {
            try {
                instance.destroy();
            } catch (Exception e) {
                System.err.println("Unable to destroy instance" + instance.get() + " for bean: "
                        + instance.getContextual());
            }
        }
        ctx.clear();
        currentContext.remove();
        currentCommandExecution.remove();
    }

    CommandExecution getCurrentCommandExecution() {
        return currentCommandExecution.get();
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

        Contextual<T> getContextual() {
            return contextual;
        }

        void destroy() {
            contextual.destroy(value, creationalContext);
        }
    }
}

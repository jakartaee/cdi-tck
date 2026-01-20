/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customContextForRequestScope;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class CustomRequestContext implements AlterableContext {
    private final ThreadLocal<Map<Contextual<?>, ContextualInstance<?>>> currentContext = new ThreadLocal<>();

    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScoped.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        Map<Contextual<?>, ContextualInstance<?>> ctx = currentContext.get();
        if (ctx == null) {
            throw new ContextNotActiveException();
        }

        ContextualInstance<T> instance = (ContextualInstance<T>) ctx.get(contextual);
        if (instance == null && creationalContext != null) {
            instance = new ContextualInstance<T>(contextual.create(creationalContext), creationalContext, contextual);
            ctx.put(contextual, instance);
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

    public void activate() {
        Map<Contextual<?>, ContextualInstance<?>> ctx = currentContext.get();
        if (ctx != null) {
            return;
        }
        currentContext.set(new HashMap<>());
    }

    public void deactivate() {
        Map<Contextual<?>, ContextualInstance<?>> ctx = currentContext.get();
        if (ctx == null) {
            return;
        }
        for (ContextualInstance<?> instance : ctx.values()) {
            try {
                instance.destroy();
            } catch (Exception e) {
                System.err.println("Unable to destroy instance" + instance.get() + " for bean: " + instance.getContextual());
            }
        }
        ctx.clear();
        currentContext.remove();
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

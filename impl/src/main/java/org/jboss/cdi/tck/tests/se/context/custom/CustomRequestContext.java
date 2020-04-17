/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.custom;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class CustomRequestContext implements AlterableContext {

    private final Map<Contextual<?>, Instance> storage = new ConcurrentHashMap<Contextual<?>, Instance>();

    @Override
    public void destroy(Contextual<?> contextual) {
        Instance instance = storage.remove(contextual);
        Contextual rawContextual = contextual;
        rawContextual.destroy(instance.instance, instance.ctx);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScoped.class;
    }

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

    private static class Instance {
        private final Object instance;
        private final CreationalContext<?> ctx;

        public Instance(Object instance, CreationalContext<?> ctx) {
            this.instance = instance;
            this.ctx = ctx;
        }
    }
}

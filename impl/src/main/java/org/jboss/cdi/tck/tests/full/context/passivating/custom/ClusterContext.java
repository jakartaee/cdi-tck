/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context.passivating.custom;

import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class ClusterContext implements Context {

    private final Map<Contextual<?>, Object> instances = new HashMap<Contextual<?>, Object>();

    private boolean isGetCalled;

    @Override
    public Class<? extends Annotation> getScope() {
        return ClusterScoped.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> ctx) {
        isGetCalled = true;
        assertTrue(contextual instanceof Serializable);
        if (ctx != null) {
            assertTrue(ctx instanceof Serializable);
        }
        synchronized (instances) {
            T instance = (T) instances.get(contextual);
            if (instance == null && ctx != null) {
                instances.put(contextual, contextual.create(ctx));
                instance = (T) instances.get(contextual);
            }
            return instance;
        }
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public boolean isGetCalled() {
        return isGetCalled;
    }

    public void reset() {
        isGetCalled = false;
    }
}

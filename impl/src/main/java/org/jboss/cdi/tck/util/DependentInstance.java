/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

/**
 * Simple helper class for creating and destroying dependent contextual instances.
 *
 * @author Martin Kouba
 * @param <T>
 */
public class DependentInstance<T> {

    private final Bean<T> bean;
    private final CreationalContext<T> creationalContext;
    private final T instance;
    private boolean destroyed = false;

    @SuppressWarnings("unchecked")
    public DependentInstance(BeanManager beanManager, Class<T> beanType, Annotation... qualifiers) {

        Set<Bean<?>> beans = beanManager.getBeans(beanType, qualifiers);

        if (beans == null || beans.isEmpty()) {
            throw new UnsatisfiedResolutionException(String.format(
                    "No bean matches required type %s and required qualifiers %s", beanType, Arrays.toString(qualifiers)));
        }

        bean = (Bean<T>) beanManager.resolve(beans);

        if (!bean.getScope().equals(Dependent.class)) {
            throw new IllegalStateException("Bean is not dependent");
        }

        creationalContext = beanManager.createCreationalContext(bean);
        instance = bean.create(creationalContext);
    }

    /**
     *
     * @return the created instance
     */
    public T get() {
        checkDestroyed();
        return instance;
    }

    /**
     * Destroy the created instance properly.
     *
     * @return self
     */
    public DependentInstance<T> destroy() {
        checkDestroyed();
        bean.destroy(instance, creationalContext);
        destroyed = true;
        return this;
    }

    /**
     * @return <code>true</code> if destroyed, <code>false</code> otherwise
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    private void checkDestroyed() {
        if (destroyed) {
            throw new IllegalStateException("Instance already destroyed");
        }
    }

}
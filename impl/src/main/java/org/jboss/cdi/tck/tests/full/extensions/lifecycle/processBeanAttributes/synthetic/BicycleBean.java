/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.synthetic;

import java.util.Collections;
import java.util.Set;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.InjectionPoint;

import org.jboss.cdi.tck.util.ForwardingBeanAttributes;

public class BicycleBean extends ForwardingBeanAttributes<Bicycle> implements Bean<Bicycle> {

    private BeanAttributes<Bicycle> delegate;

    public BicycleBean(BeanAttributes<Bicycle> delegate) {
        this.delegate = delegate;
    }

    public Bicycle create(CreationalContext<Bicycle> creationalContext) {
        return new Bicycle();
    }

    public void destroy(Bicycle instance, CreationalContext<Bicycle> creationalContext) {
    }

    public Class<?> getBeanClass() {
        return Bicycle.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    protected BeanAttributes<Bicycle> attributes() {
        return delegate;
    }

}

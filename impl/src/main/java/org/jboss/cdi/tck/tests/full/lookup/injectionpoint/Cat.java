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
package org.jboss.cdi.tck.tests.full.lookup.injectionpoint;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@Dependent
public class Cat implements Animal {
    @Inject
    private InjectionPoint injectionPoint;

    @Inject
    private BeanManager beanManager;

    public String hello() {
        return "hello";
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    @Override
    public InjectionPoint getDecorator1InjectionPoint() {
        return null;
    }

    @Override
    public InjectionPoint getDecorator2InjectionPoint() {
        return null;
    }

    @Override
    public InjectionPoint getDecorator3InjectionPoint() {
        return null;
    }

    @Override
    public Toy getToy() {
        return null;
    }
}

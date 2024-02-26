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
package org.jboss.cdi.tck.tests.full.extensions.beanManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class InjectionPointDecorator implements InjectionPoint {
    private final InjectionPoint injectionPoint;

    public InjectionPointDecorator(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public Annotated getAnnotated() {
        return injectionPoint.getAnnotated();
    }

    public Bean<?> getBean() {
        return injectionPoint.getBean();
    }

    public Set<Annotation> getQualifiers() {
        return injectionPoint.getQualifiers();
    }

    public Member getMember() {
        return injectionPoint.getMember();
    }

    public Type getType() {
        return Dog.class;
    }

    public boolean isDelegate() {
        return injectionPoint.isDelegate();
    }

    public boolean isTransient() {
        return injectionPoint.isTransient();
    }
}

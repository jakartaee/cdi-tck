/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.full.lookup.injectionpoint.broken.reference.unresolved;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class UnsatisfiedInjectionPoint implements InjectionPoint {

    private final Bean<SimpleBean> bean;
    private final Set<Annotation> bindings = new HashSet<Annotation>();

    public UnsatisfiedInjectionPoint(Bean<SimpleBean> beanWithInjectionPoint) {
        this.bean = beanWithInjectionPoint;
        bindings.add(Default.Literal.INSTANCE);
        bindings.add(Any.Literal.INSTANCE);
    }

    public Annotated getAnnotated() {
        return new AnnotatedInjectionField(this);
    }

    public Bean<?> getBean() {
        return bean;
    }

    public Set<Annotation> getQualifiers() {
        return bindings;
    }

    @SuppressWarnings("unchecked")
    public Member getMember() {
        return ((AnnotatedField<SimpleBean>) getAnnotated()).getJavaMember();
    }

    public Type getType() {
        return InjectedBean.class;
    }

    public boolean isDelegate() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

}

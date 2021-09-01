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
package org.jboss.cdi.tck.tests.lookup.injectionpoint.broken.reference.ambiguous;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class AnnotatedInjectionField implements AnnotatedField<InjectedBean> {

    private final InjectionPoint injectionPoint;

    public AnnotatedInjectionField(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public Field getJavaMember() {
        try {
            return SimpleBean.class.getDeclaredField("injectedBean");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field for injectedBean", e);
        }
    }

    public AnnotatedType<InjectedBean> getDeclaringType() {
        return null;
    }

    public boolean isStatic() {
        return false;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return null;
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> aClass) {
        return null;
    }

    public Set<Annotation> getAnnotations() {
        return null;
    }

    public Type getBaseType() {
        return InjectedBean.class;
    }

    public Set<Type> getTypeClosure() {
        return null;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return false;
    }

}

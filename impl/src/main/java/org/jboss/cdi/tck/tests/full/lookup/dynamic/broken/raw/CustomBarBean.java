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
package org.jboss.cdi.tck.tests.full.lookup.dynamic.broken.raw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class CustomBarBean implements Bean<Bar> {

    public static CustomInstanceInjectionPoint instanceInjectionPoint;

    public CustomBarBean(AnnotatedField<?> annotatedField) {
        instanceInjectionPoint = new CustomInstanceInjectionPoint(this, annotatedField);
    }

    @Override
    public Bar create(CreationalContext<Bar> creationalContext) {
        return new Bar("Baz");
    }

    @Override
    public void destroy(Bar instance, CreationalContext<Bar> creationalContext) {
        creationalContext.release();
    }

    @Override
    public Set<Type> getTypes() {
        HashSet<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(Bar.class);
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<Annotation>();
        qualifiers.add(Any.Literal.INSTANCE);
        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return SessionScoped.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Class<?> getBeanClass() {
        return Bar.class;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.<InjectionPoint> singleton(instanceInjectionPoint);
    }

}

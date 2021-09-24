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
package org.jboss.cdi.tck.tests.full.definition.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;

public class FooBean implements Bean<Foo>, PassivationCapable {

    public static CustomInjectionPoint integerInjectionPoint;

    public static CustomInjectionPoint barInjectionPoint;

    public FooBean(AnnotatedField<?> integerAnnotatedField, AnnotatedField<?> barAnnotatedField, boolean isTransient) {
        integerInjectionPoint = new CustomInjectionPoint(Integer.class, this, isTransient, integerAnnotatedField);
        barInjectionPoint = new CustomInjectionPoint(Bar.class, this, isTransient, barAnnotatedField);
    }

    @Override
    public Foo create(CreationalContext<Foo> creationalContext) {
        return new Foo();
    }

    @Override
    public void destroy(Foo instance, CreationalContext<Foo> creationalContext) {
        creationalContext.release();
    }

    @Override
    public Set<Type> getTypes() {
        HashSet<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(Foo.class);
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return Collections.singleton((Annotation) new PassivableLiteral());
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
        return Foo.class;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        Set<InjectionPoint> injectionPoints = new HashSet<InjectionPoint>(2);
        injectionPoints.add(integerInjectionPoint);
        injectionPoints.add(barInjectionPoint);
        return injectionPoints;
    }

    @Override
    public String getId() {
        return AfterBeanDiscoveryObserver.class.getName() + ":" + FooBean.class.getName();
    }

}

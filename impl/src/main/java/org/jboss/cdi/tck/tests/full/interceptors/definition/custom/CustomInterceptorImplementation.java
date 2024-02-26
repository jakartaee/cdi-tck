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
package org.jboss.cdi.tck.tests.full.interceptors.definition.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.interceptor.InvocationContext;

public class CustomInterceptorImplementation implements Interceptor<SimpleInterceptorWithoutAnnotations> {

    private Set<Annotation> interceptorBindingTypes = new HashSet<Annotation>();
    private InterceptionType type;
    private boolean getInterceptorBindingsCalled = false;
    private boolean interceptsCalled = false;

    public CustomInterceptorImplementation(InterceptionType type) {
        this.type = type;
        interceptorBindingTypes.add(new Secure.Literal());
        interceptorBindingTypes.add(new Transactional.Literal());
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public String getName() {
        return null;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.emptySet();
    }

    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(getBeanClass());
        return types;
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() {
        return false;
    }

    public Object intercept(InterceptionType type, SimpleInterceptorWithoutAnnotations instance, InvocationContext ctx) {
        try {
            return instance.intercept(ctx);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean intercepts(InterceptionType type) {
        interceptsCalled = true;
        return this.type.equals(type);
    }

    public Set<Annotation> getInterceptorBindings() {
        getInterceptorBindingsCalled = true;
        return Collections.unmodifiableSet(interceptorBindingTypes);
    }

    public Class<?> getBeanClass() {
        return SimpleInterceptorWithoutAnnotations.class;
    }

    public SimpleInterceptorWithoutAnnotations create(
            CreationalContext<SimpleInterceptorWithoutAnnotations> creationalContext) {
        return new SimpleInterceptorWithoutAnnotations();
    }

    public void destroy(SimpleInterceptorWithoutAnnotations instance,
            CreationalContext<SimpleInterceptorWithoutAnnotations> creationalContext) {
        creationalContext.release();
    }

    public boolean isGetInterceptorBindingsCalled() {
        return getInterceptorBindingsCalled;
    }

    public boolean isInterceptsCalled() {
        return interceptsCalled;
    }
}

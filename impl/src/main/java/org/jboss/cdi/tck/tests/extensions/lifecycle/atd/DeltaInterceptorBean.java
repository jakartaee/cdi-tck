/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.atd;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Dependent
public class DeltaInterceptorBean implements Interceptor<DeltaInterceptor> {

    public Set<Annotation> getInterceptorBindings() {
        return Collections.<Annotation>singleton(Monitored.MonitoredBindingLiteral.INSTANCE);
    }

    public boolean intercepts(InterceptionType type) {
        return InterceptionType.AROUND_INVOKE.equals(type);
    }

    public Object intercept(InterceptionType type, DeltaInterceptor instance, InvocationContext ctx) throws Exception {
        return instance.monitor(ctx);
    }

    public Class<?> getBeanClass() {
        return DeltaInterceptor.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public boolean isNullable() {
        return false;
    }

    public Set<Type> getTypes() {
        HashSet<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(DeltaInterceptor.class);
        return types;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.emptySet();
    }

    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public String getName() {
        return DeltaInterceptor.class.getName();
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public boolean isAlternative() {
        return false;
    }

    public DeltaInterceptor create(CreationalContext<DeltaInterceptor> creationalContext) {
        return new DeltaInterceptor();
    }

    public void destroy(DeltaInterceptor instance, CreationalContext<DeltaInterceptor> creationalContext) {
        creationalContext.release();
    }
}

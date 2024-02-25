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
package org.jboss.cdi.tck.tests.full.context;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;

public class MyContextual implements Bean<MySessionBean>, PassivationCapable {
    private boolean createCalled = false;
    private boolean destroyCalled = false;
    private boolean shouldReturnNullInstances = false;

    protected MyContextual(BeanManager beanManager) {
    }

    public Set<Annotation> getQualifiers() {
        return Collections.emptySet();
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public String getName() {
        return "my-session-bean";
    }

    public Class<? extends Annotation> getScope() {
        return SessionScoped.class;
    }

    @SuppressWarnings("unchecked")
    public Set<Type> getTypes() {
        return new HashSet<Type>(Arrays.asList(Object.class, MySessionBean.class, Serializable.class));
    }

    public boolean isNullable() {
        return false;
    }

    public MySessionBean create(CreationalContext<MySessionBean> creationalContext) {
        createCalled = true;
        if (shouldReturnNullInstances)
            return null;
        else
            return new MySessionBean();
    }

    public void destroy(MySessionBean instance, CreationalContext<MySessionBean> creationalContext) {
        destroyCalled = true;
    }

    public boolean isCreateCalled() {
        return createCalled;
    }

    public boolean isDestroyCalled() {
        return destroyCalled;
    }

    public void setShouldReturnNullInstances(boolean shouldReturnNullInstances) {
        this.shouldReturnNullInstances = shouldReturnNullInstances;
    }

    public Class<?> getBeanClass() {
        return MySessionBean.class;
    }

    public boolean isAlternative() {
        return false;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public String getId() {
        return "org.jboss.cdi.tck.tests.context.myContextual";
    }

}

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
package org.jboss.cdi.tck.tests.definition.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;

public class IntegerBean implements Bean<Integer>, PassivationCapable {

    private boolean getQualifiersCalled = false;
    private boolean getInjectionPointsCalled = false;
    private boolean getNameCalled = false;
    private boolean getScopeCalled = false;
    private boolean getTypesCalled = false;
    private boolean isAlternativeCalled = false;
    private boolean isSerializableCalled = false;
    private boolean isGetBeanClassCalled = false;
    private boolean getStereotypesCalled = false;

    public Class<?> getBeanClass() {
        isGetBeanClassCalled = true;
        return Integer.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        getInjectionPointsCalled = true;
        return Collections.emptySet();
    }

    public String getName() {
        getNameCalled = true;
        return "one";
    }

    @SuppressWarnings("serial")
    public Set<Annotation> getQualifiers() {
        getQualifiersCalled = true;
        return new HashSet<Annotation>() {
            {
                add(Default.Literal.INSTANCE);
            }
        };
    }

    public Class<? extends Annotation> getScope() {
        getScopeCalled = true;
        return Dependent.class;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        // Spec needs clarification - see CDITCK-273
        // HashSet<Class<? extends Annotation>> stereotypes = new HashSet<Class<? extends Annotation>>();
        // stereotypes.add(AlternativeStereotype.class);
        getStereotypesCalled = true;
        return Collections.emptySet();
    }

    public Set<Type> getTypes() {
        HashSet<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(Number.class);
        types.add(Integer.class);

        getTypesCalled = true;
        return types;
    }

    public boolean isAlternative() {
        isAlternativeCalled = true;
        return true;
    }

    public boolean isNullable() {
        return false;
    }

    public Integer create(CreationalContext<Integer> creationalContext) {
        return new Integer(1);
    }

    public void destroy(Integer instance, CreationalContext<Integer> creationalContext) {
        creationalContext.release();
    }

    public boolean isGetQualifiersCalled() {
        return getQualifiersCalled;
    }

    public boolean isGetInjectionPointsCalled() {
        return getInjectionPointsCalled;
    }

    public boolean isGetNameCalled() {
        return getNameCalled;
    }

    public boolean isGetScopeCalled() {
        return getScopeCalled;
    }

    public boolean isGetTypesCalled() {
        return getTypesCalled;
    }

    public boolean isAlternativeCalled() {
        return isAlternativeCalled;
    }

    public boolean isSerializableCalled() {
        return isSerializableCalled;
    }

    public boolean isGetBeanClassCalled() {
        return isGetBeanClassCalled;
    }

    public boolean isGetStereotypesCalled() {
        return getStereotypesCalled;
    }

    @Override
    public String getId() {
        return AfterBeanDiscoveryObserver.class.getName() + ":" + IntegerBean.class.getName();
    }

}

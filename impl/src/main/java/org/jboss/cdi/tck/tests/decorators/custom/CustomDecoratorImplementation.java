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
package org.jboss.cdi.tck.tests.decorators.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.cdi.tck.literals.DefaultLiteral;

public class CustomDecoratorImplementation implements Decorator<VehicleDecorator> {

    private boolean getDecoratedTypesCalled = false;
    private boolean getDelegateQualifiersCalled = false;
    private boolean getDelegateTypeCalled = false;

    private AnnotatedField<? super VehicleDecorator> annotatedField;
    private Set<Type> decoratedTypes;
    private Set<InjectionPoint> injectionPoints;
    private BeanManager beanManager;

    public CustomDecoratorImplementation(AnnotatedField<? super VehicleDecorator> annotatedField, BeanManager beanManager) {
        this.annotatedField = annotatedField;
        this.beanManager = beanManager;
        decoratedTypes = Collections.singleton((Type) Vehicle.class);
        injectionPoints = Collections.singleton((InjectionPoint) new CustomInjectionPoint());
    }

    public Set<Type> getDecoratedTypes() {
        getDecoratedTypesCalled = true;
        return decoratedTypes;
    }

    public Set<Annotation> getDelegateQualifiers() {
        getDelegateQualifiersCalled = true;
        return Collections.emptySet();
    }

    public Type getDelegateType() {
        getDelegateTypeCalled = true;
        return Vehicle.class;
    }

    public Class<?> getBeanClass() {
        return VehicleDecorator.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return injectionPoints;
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
        return new HashSet<Type>(Arrays.asList(VehicleDecorator.class, Object.class));
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() {
        return false;
    }

    public VehicleDecorator create(CreationalContext<VehicleDecorator> ctx) {
        VehicleDecorator decorator = new VehicleDecorator();
        decorator.delegate = (Vehicle) beanManager.getInjectableReference(getInjectionPoints().iterator().next(), ctx);
        return decorator;
    }

    public void destroy(VehicleDecorator arg0, CreationalContext<VehicleDecorator> arg1) {
        arg1.release();
    }

    public boolean isGetDecoratedTypesCalled() {
        return getDecoratedTypesCalled;
    }

    public boolean isGetDelegateQualifiersCalled() {
        return getDelegateQualifiersCalled;
    }

    public boolean isGetDelegateTypeCalled() {
        return getDelegateTypeCalled;
    }

    class CustomInjectionPoint implements InjectionPoint {

        public boolean isTransient() {
            return false;
        }

        public boolean isDelegate() {
            return true;
        }

        public Type getType() {
            return Vehicle.class;
        }

        public Set<Annotation> getQualifiers() {
            return Collections.singleton((Annotation) new DefaultLiteral());
        }

        public Member getMember() {
            return annotatedField.getJavaMember();
        }

        public Bean<?> getBean() {
            return CustomDecoratorImplementation.this;
        }

        public Annotated getAnnotated() {
            return annotatedField;
        }
    };
}

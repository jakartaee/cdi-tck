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
package org.jboss.cdi.tck.tests.full.decorators.custom.broken.toomanydelegateinjectionpoints;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Decorator;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class CustomDecorator implements Decorator<VehicleDecorator> {

    private AnnotatedField<? super VehicleDecorator> annotatedField;
    private Set<Type> decoratedTypes;
    private Set<InjectionPoint> injectionPoints;
    private BeanManager beanManager;

    public CustomDecorator(AnnotatedField<? super VehicleDecorator> annotatedField, BeanManager beanManager) {
        this.annotatedField = annotatedField;
        this.beanManager = beanManager;
        decoratedTypes = Collections.<Type> singleton(Vehicle.class);

        injectionPoints = new HashSet<InjectionPoint>();
        injectionPoints.add(new CustomInjectionPoint());
        injectionPoints.add(new CustomInjectionPoint());
    }

    public Set<Type> getDecoratedTypes() {
        return decoratedTypes;
    }

    public Set<Annotation> getDelegateQualifiers() {
        return Collections.emptySet();
    }

    public Type getDelegateType() {
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

    @SuppressWarnings("unchecked")
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
            return Collections.<Annotation> singleton(Default.Literal.INSTANCE);
        }

        public Member getMember() {
            return annotatedField.getJavaMember();
        }

        public Bean<?> getBean() {
            return CustomDecorator.this;
        }

        public Annotated getAnnotated() {
            return annotatedField;
        }
    };
}

/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.inject.Default;

public class DeltaDecoratorBean implements Decorator<DeltaDecorator>, Prioritized {

    private AnnotatedField<? super DeltaDecorator> annotatedField = null;
    private BeanManager beanManager;

    public DeltaDecoratorBean(AnnotatedField<? super DeltaDecorator> annotatedField, BeanManager bm) {
        this.annotatedField = annotatedField;
        this.beanManager = bm;
    }

    public Type getDelegateType() {
        return Logger.class;
    }

    public Set<Annotation> getDelegateQualifiers() {
        return Collections.emptySet();
    }

    public Set<Type> getDecoratedTypes() {
        return Collections.<Type>singleton(Logger.class);
    }

    public Class<?> getBeanClass() {
        return DeltaDecorator.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        HashSet<InjectionPoint> injectionPoints = new HashSet<InjectionPoint>();
        injectionPoints.add(new CustomInjectionPoint());
        return injectionPoints;
    }

    @Override
    public int getPriority() {
        return 2011;
    }

    public boolean isNullable() {
        return false;
    }

    public Set<Type> getTypes() {
        HashSet<Type> types = new HashSet<Type>();
        types.add(Object.class);
        types.add(DeltaDecorator.class);
        types.add(Logger.class);
        return types;
    }

    public Set<Annotation> getQualifiers() {
        return Collections.emptySet();
    }

    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public String getName() {
        return DeltaDecorator.class.getName();
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public boolean isAlternative() {
        return false;
    }

    public DeltaDecorator create(CreationalContext<DeltaDecorator> creationalContext) {
        DeltaDecorator decorator = new DeltaDecorator();
        decorator.logger = (Logger) beanManager.getInjectableReference(getInjectionPoints().iterator().next(), creationalContext);
        return decorator;
    }

    public void destroy(DeltaDecorator instance, CreationalContext<DeltaDecorator> creationalContext) {
        creationalContext.release();
    }

    class CustomInjectionPoint implements InjectionPoint {

        public Type getType() {
            return Logger.class;
        }

        public Set<Annotation> getQualifiers() {
            return Collections.<Annotation>singleton(Default.Literal.INSTANCE);
        }

        public Bean<?> getBean() {
            return DeltaDecoratorBean.this;
        }

        public Member getMember() {
            return annotatedField.getJavaMember();
        }

        public Annotated getAnnotated() {
            return annotatedField;
        }

        public boolean isDelegate() {
            return true;
        }

        public boolean isTransient() {
            return false;
        }
    }
}

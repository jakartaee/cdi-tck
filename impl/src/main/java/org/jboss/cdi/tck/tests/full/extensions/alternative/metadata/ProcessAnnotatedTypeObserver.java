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
package org.jboss.cdi.tck.tests.full.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension {

    public void observeGroceryAnnotatedType(@Observes ProcessAnnotatedType<Grocery> event) {
        event.setAnnotatedType(new GroceryWrapper(event.getAnnotatedType()));
    }

    public void observeMarketAnnotatedType(@Observes ProcessAnnotatedType<Market> event) {
        event.setAnnotatedType(new MarketWrapper(event.getAnnotatedType()));
    }

    public void observeSausageAnnotatedType(@Observes ProcessAnnotatedType<Sausage> event) {

        final AnnotatedConstructor<Sausage> originalConstructor = event.getAnnotatedType().getConstructors().iterator().next();

        AnnotatedType<Sausage> overridingSausage = new AnnotatedType<Sausage>() {
            @Override
            public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
                return Collections.emptySet();
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return false;
            }

            @Override
            public Set<Type> getTypeClosure() {
                Set<Type> typeClosure = new HashSet<Type>();
                typeClosure.add(Sausage.class);
                typeClosure.add(Object.class);
                return typeClosure;
            }

            @Override
            public Type getBaseType() {
                return Sausage.class;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                // No annotations
                return Collections.emptySet();
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
                // No annotations
                return null;
            }

            @Override
            public Set<AnnotatedMethod<? super Sausage>> getMethods() {
                return Collections.emptySet();
            }

            @Override
            public Class<Sausage> getJavaClass() {
                return Sausage.class;
            }

            @Override
            public Set<AnnotatedField<? super Sausage>> getFields() {
                return Collections.emptySet();
            }

            @Override
            public Set<AnnotatedConstructor<Sausage>> getConstructors() {
                return Collections.singleton(originalConstructor);
            }
        };
        event.setAnnotatedType(overridingSausage);
    }
}

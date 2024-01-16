/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.decorators.builtin.beanmanager;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.concurrent.CompletionStage;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.NotificationOptions;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 * 
 */
@Decorator
@SuppressWarnings("serial")
public abstract class BeanManagerDecorator implements BeanManager, Serializable {

    @Inject
    @Delegate
    BeanManager beanManager;

    @Override
    public Event<Object> getEvent() {
        Event<Object> delegate = beanManager.getEvent();
        return new Event<Object>() {
            @Override
            public void fire(Object event) {
                if (Foo.class.isAssignableFrom(event.getClass())) {
                    ((Foo) event).setDecorated(true);
                }
            }

            @Override
            public <U> CompletionStage<U> fireAsync(U event) {
                return delegate.fireAsync(event);
            }

            @Override
            public <U> CompletionStage<U> fireAsync(U event, NotificationOptions options) {
                return delegate.fireAsync(event, options);
            }

            @Override
            public Event<Object> select(Annotation... qualifiers) {
                return delegate.select(qualifiers);
            }

            @Override
            public <U> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
                return delegate.select(subtype, qualifiers);
            }

            @Override
            public <U> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
                return delegate.select(subtype, qualifiers);
            }
        };
    }

    @Override
    public boolean isQualifier(Class<? extends Annotation> annotationType) {
        return false;
    }
}

/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.full.decorators.builtin.event;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Decorator
@SuppressWarnings("serial")
public abstract class CharSequenceEventDecorator<T extends CharSequence> implements Event<T>, Serializable {

    @SuppressWarnings("unused")
    @Inject
    @Delegate
    private Event<T> delegate;

    @Inject
    private BeanManager manager;

    @Override
    public void fire(CharSequence event) {
        manager.getEvent().select(String.class).fire("DecoratedCharSequence" + event.toString());
    }

    @Override
    public Event<T> select(Annotation... qualifiers) {
        throw new UnsupportedOperationException();
    }

}

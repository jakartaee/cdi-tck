/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.observer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Reception;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.ObserverMethod;

public class EventBObserverMethod implements ObserverMethod<EventB> {
    @Override
    public Class<?> getBeanClass() {
        return EventObserver.class;
    }

    @Override
    public Type getObservedType() {
        return EventC.class;
    }

    @Override
    public Set<Annotation> getObservedQualifiers() {
        Set<Annotation> qualifiers = new HashSet<>();
        qualifiers.add(Any.Literal.INSTANCE);
        return qualifiers;
    }

    @Override
    public Reception getReception() {
        return Reception.ALWAYS;
    }

    @Override
    public TransactionPhase getTransactionPhase() {
        return TransactionPhase.IN_PROGRESS;
    }

    @Override
    public void notify(EventB event) {

    }
}

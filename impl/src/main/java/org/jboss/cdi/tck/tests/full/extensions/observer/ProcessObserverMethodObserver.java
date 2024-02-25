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
package org.jboss.cdi.tck.tests.full.extensions.observer;

import java.lang.reflect.Type;
import java.util.HashSet;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;

public class ProcessObserverMethodObserver implements Extension {
    private static final HashSet<Type> eventTypes = new HashSet<Type>();
    private static AnnotatedMethod<?> annotatedMethod;
    private static ObserverMethod<?> observerMethod;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        annotatedMethod = null;
        observerMethod = null;
    }

    /**
     * https://issues.jboss.org/browse/CDI-88
     *
     * @param event
     */
    public void observeObserverMethodForEventA(@Observes ProcessObserverMethod<EventA, EventObserver> event) {
        eventTypes.add(event.getObserverMethod().getObservedType());
        annotatedMethod = event.getAnnotatedMethod();
        observerMethod = event.getObserverMethod();
    }

    public void observeObserverMethodForEventB(@Observes ProcessObserverMethod<EventB, EventObserver> event) {
        event.setObserverMethod(new EventBObserverMethod());
    }

    public void observeObserverMethodForEventD(@Observes ProcessObserverMethod<EventD, EventObserver> event) {
        event.veto();
    }

    public static HashSet<Type> getEventtypes() {
        return eventTypes;
    }

    public static AnnotatedMethod<?> getAnnotatedMethod() {
        return annotatedMethod;
    }

    public static ObserverMethod<?> getObserverMethod() {
        return observerMethod;
    }

}

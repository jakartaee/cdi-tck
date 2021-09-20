/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.priority.contextLifecycleEvent;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.interceptor.Interceptor;

import org.jboss.cdi.tck.util.ActionSequence;

@Dependent
public class ApplicationScopedObserver {

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String D = "D";

    public void first(@Observes @Initialized(ApplicationScoped.class) @Priority(Interceptor.Priority.APPLICATION - 100) Object obj) {
        ActionSequence.addAction(A);
    }

    public void second(@Observes @Initialized(ApplicationScoped.class) Object obj) {
        ActionSequence.addAction(B);
    }

    public void third(@Observes @Initialized(ApplicationScoped.class) @Priority(Interceptor.Priority.APPLICATION + 600) Object obj) {
        ActionSequence.addAction(C);
    }

    public void forth(@Observes @Initialized(ApplicationScoped.class) @Priority(Interceptor.Priority.APPLICATION + 700) Object obj) {
        ActionSequence.addAction(D);
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.custom;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.BeforeDestroyed;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class CustomRequestContextObserver {

    public static boolean initializedCalled = false;
    public static boolean beforeDestroyedCalled = false;
    public static boolean destroyedCalled = false;

    public void observesRequestContextInitialization(@Observes @Initialized(RequestScoped.class) Object object) {
        initializedCalled = true;
    }

    public void observesRequestContextBeforeDestroyed(@Observes @BeforeDestroyed(RequestScoped.class) Object object) {
        beforeDestroyedCalled = true;
    }

    public void observesRequestContextDestroyed(@Observes @Destroyed(RequestScoped.class) Object object) {
        destroyedCalled = true;
    }
}

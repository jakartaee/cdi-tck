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
package org.jboss.cdi.tck.tests.se.context.activation.interceptor;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class RequestContextObserver {

    private final AtomicInteger initCounter = new AtomicInteger(0);
    private final AtomicInteger beforeDestroyedCounter = new AtomicInteger(0);
    private final AtomicInteger destroyedCounter = new AtomicInteger(0);

    public void observesRequestContextInitialization(@Observes @Initialized(RequestScoped.class) Object object) {
        initCounter.incrementAndGet();
    }

    public void observesRequestContextBeforeDestroyed(@Observes @BeforeDestroyed(RequestScoped.class) Object object) {
        beforeDestroyedCounter.incrementAndGet();
    }

    public void observesRequestContextDestroyed(@Observes @Destroyed(RequestScoped.class) Object object) {
        destroyedCounter.incrementAndGet();
    }

    public int getInitCounter() {
        return initCounter.get();
    }

    public int getBeforeDestroyedCounter() {
        return beforeDestroyedCounter.get();
    }

    public int getDestroyedCounter() {
        return destroyedCounter.get();
    }
}

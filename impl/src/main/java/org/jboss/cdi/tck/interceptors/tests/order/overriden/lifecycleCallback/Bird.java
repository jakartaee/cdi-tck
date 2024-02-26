/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.order.overriden.lifecycleCallback;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class Bird {

    private static AtomicInteger initBirdCalled = new AtomicInteger();
    private static AtomicInteger destroyBirdCalled = new AtomicInteger();

    @PostConstruct
    public void init() {
        initBirdCalled.incrementAndGet();
    }

    @PreDestroy
    public void destroy() {
        destroyBirdCalled.incrementAndGet();
    }

    public static void reset() {
        initBirdCalled.set(0);
        destroyBirdCalled.set(0);
    }

    public static AtomicInteger getInitBirdCalled() {
        return initBirdCalled;
    }

    public static AtomicInteger getDestroyBirdCalled() {
        return destroyBirdCalled;
    }

}

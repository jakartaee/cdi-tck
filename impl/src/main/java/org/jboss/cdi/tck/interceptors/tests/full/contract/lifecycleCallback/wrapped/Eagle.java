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
package org.jboss.cdi.tck.interceptors.tests.full.contract.lifecycleCallback.wrapped;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class Eagle extends Bird {

    private static AtomicInteger initEagleCalled = new AtomicInteger();
    private static AtomicInteger destroyEagleCalled = new AtomicInteger();

    @Inject
    private void initEagleBar(Bar bar) {
        eagleBar = bar;
    }

    @PostConstruct
    public void initEagle() {
        initEagleCalled.incrementAndGet();
    }

    @PreDestroy
    public void destroyEagle() {
        destroyEagleCalled.incrementAndGet();
    }

    public void ping() {
    }

    public static AtomicInteger getInitEagleCalled() {
        return initEagleCalled;
    }

    public static AtomicInteger getDestroyEagleCalled() {
        return destroyEagleCalled;
    }

    public static void reset() {
        initEagleCalled.set(0);
        destroyEagleCalled.set(0);
    }

}

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
package org.jboss.cdi.tck.tests.full.interceptors.contract.lifecycleCallback.wrapped;

import static org.testng.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class Bird {

    private static AtomicInteger initBirdCalled = new AtomicInteger();
    private static AtomicInteger destroyBirdCalled = new AtomicInteger();

    protected Bar eagleBar;

    private Bar birdBar;

    @Inject
    private void initBirdBar(Bar bar) {
        birdBar = bar;
    }

    @PostConstruct
    public void initBird() {
        // assert that injection on Eagle instance has been completed
        assertNotNull(birdBar);
        assertNotNull(eagleBar);
        initBirdCalled.incrementAndGet();
    }

    @PreDestroy
    public void destroyBird() {
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

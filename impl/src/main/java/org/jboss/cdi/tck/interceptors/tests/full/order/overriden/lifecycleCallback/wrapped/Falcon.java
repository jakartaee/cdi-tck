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
package org.jboss.cdi.tck.interceptors.tests.full.order.overriden.lifecycleCallback.wrapped;

import java.util.concurrent.atomic.AtomicInteger;

public class Falcon extends Bird {

    private static AtomicInteger initFalconCalled = new AtomicInteger();
    private static AtomicInteger destroyFalconCalled = new AtomicInteger();

    @Override
    public void init() {
        initFalconCalled.incrementAndGet();
    }

    @Override
    public void destroy() {
        destroyFalconCalled.incrementAndGet();
    }

    public void ping() {
    }

    public static AtomicInteger getInitFalconCalled() {
        return initFalconCalled;
    }

    public static AtomicInteger getDestroyFalconCalled() {
        return destroyFalconCalled;
    }

    public static void reset() {
        initFalconCalled.set(0);
        destroyFalconCalled.set(0);
    }

}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.request.event.timeout;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ApplicationScopedObserver {

    @Inject
    private RequestScopedObserver observer;

    private final AtomicBoolean destroyedCalled = new AtomicBoolean();
    private SynchronousQueue<Boolean> queue = new SynchronousQueue<Boolean>();

    void observeRequestDestroyed(@Observes @Destroyed(RequestScoped.class) Object event) {
        destroyedCalled.set(true);
    }

    void reset() {
        destroyedCalled.set(false);
    }

    boolean isDestroyedCalled() {
        return destroyedCalled.get();
    }

    public void offerQueue() {
        queue.offer(observer.isInitializedObserved());
    }

    public Boolean pollQueue(long timeout, TimeUnit timeUnit) {
        Boolean result = new Boolean(false);
        try {
            result = queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

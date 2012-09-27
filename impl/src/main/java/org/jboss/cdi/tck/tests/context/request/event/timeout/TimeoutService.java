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

package org.jboss.cdi.tck.tests.context.request.event.timeout;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

@Stateless
public class TimeoutService {

    @Resource
    private TimerService timerService;

    @Inject
    private RequestScopedObserver observer;

    @Inject
    private ApplicationScopedObserver appObserver;

    public void start(SynchronousQueue<Boolean> queue) {
        TimerConfig config = new TimerConfig(queue, false);
        timerService.createSingleActionTimer(100, config);
    }

    @Timeout
    public void onTimeout(Timer timer) {
        if (timer.getInfo() instanceof SynchronousQueue<?>) {
            appObserver.reset();
            @SuppressWarnings("unchecked")
            SynchronousQueue<Boolean> queue = (SynchronousQueue<Boolean>) timer.getInfo();
            try {
                queue.offer(observer.isInitializedObserved(), 5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment;

import static org.testng.Assert.assertEquals;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors(AroundTimeoutThreadInterceptor.class)
public class Bar {

    @Interceptors(AroundInvokeThreadInterceptor.class)
    public int ping() {
        assertEquals(Thread.currentThread().getName(), "abc");
        Thread.currentThread().setName("def");
        return 1;
    }

    public static Long timeoutAt = null;

    @Resource
    private SessionContext ctx;

    public void createTimer() {
        ctx.getTimerService().createTimer(1l, null);
    }

    @Timeout
    public void ejbTimeout(Timer timer) {
        assertEquals(Thread.currentThread().getName(), "abc");
        Thread.currentThread().setName("def");
        timeoutAt = System.currentTimeMillis();
    }
}

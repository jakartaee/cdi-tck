/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle;

import static org.testng.Assert.assertNotNull;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@BazBinding
@Priority(1000)
public class PreDestroyInterceptor {

    public static boolean called = false;

    @Inject
    private Bar bar;

    @PreDestroy
    public void intercept(InvocationContext ctx) {
        called = true;
        assertNotNull(bar);
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

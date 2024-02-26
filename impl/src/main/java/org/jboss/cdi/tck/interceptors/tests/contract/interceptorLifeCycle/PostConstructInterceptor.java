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

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@BazBinding
@Priority(1000)
public class PostConstructInterceptor {

    public static boolean called = false;

    @Inject
    private Bar bar;

    @PostConstruct
    public void intercept(InvocationContext ctx) {
        called = true;
        assertNotNull(bar);
        // check dependency injection performed on target instace - this check is sufficient
        // in the first interceptor called
        assertNotNull(((Baz) ctx.getTarget()).bar);
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

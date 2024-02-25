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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.aroundConstruct;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@BazBinding
@Priority(Interceptor.Priority.APPLICATION)
public class Baz2Interceptor {

    private static boolean proceed = true;

    @AroundConstruct
    public void intercept(InvocationContext ctx) {
        if (proceed) {
            try {
                ctx.proceed();
                Baz instance = (Baz) ctx.getTarget();
                instance.accessed = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setProceed(boolean proceed) {
        Baz2Interceptor.proceed = proceed;
    }
}

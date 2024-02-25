/*
 * Copyright 2018, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.massOperations;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.Monitored;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Interceptor
@Priority(2700)
@Monitored
public class GammaInterceptor {

    private static boolean intercepted = false;

    @AroundInvoke
    public Object monitor(InvocationContext ctx) throws Exception {
        intercepted = true;
        return ctx.proceed();
    }

    public static void reset() {
        intercepted = false;
    }

    public static boolean isIntercepted() {
        return intercepted;
    }
}

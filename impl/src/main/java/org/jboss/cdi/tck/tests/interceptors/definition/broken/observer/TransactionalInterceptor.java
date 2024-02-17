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
package org.jboss.cdi.tck.tests.interceptors.definition.broken.observer;

import jakarta.annotation.Priority;
import jakarta.enterprise.event.Observes;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.cdi.tck.util.SimpleLogger;

@Transactional
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class TransactionalInterceptor {

    private static final SimpleLogger logger = new SimpleLogger(TransactionalInterceptor.class);

    @AroundInvoke
    public Object alwaysReturnThis(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }

    public void observeFoo(@Observes FooPayload fooPayload) {
        logger.log("OBSERVED");
    }
}

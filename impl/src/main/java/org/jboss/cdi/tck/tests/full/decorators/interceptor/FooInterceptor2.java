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
package org.jboss.cdi.tck.tests.full.decorators.interceptor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.cdi.tck.util.ActionSequence;

@Interceptor
@FooBinding2
public class FooInterceptor2 {

    public static String NAME = FooInterceptor2.class.getSimpleName();

    @AroundInvoke
    public Object interceptMe(InvocationContext ctx) throws Exception {
        ActionSequence.addAction(NAME);
        return ctx.proceed();
    }

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        if (ctx.getTarget() instanceof Foo) {
            ActionSequence.addAction("postConstruct", NAME);
        }
        try {
            ctx.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void preDestroy(InvocationContext ctx) {
        if (ctx.getTarget() instanceof Foo) {
            ActionSequence.addAction("preDestroy", NAME);
        }
        try {
            ctx.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

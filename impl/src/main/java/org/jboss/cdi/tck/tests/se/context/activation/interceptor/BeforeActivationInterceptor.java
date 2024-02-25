/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.activation.interceptor;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@BeforeActivationBinding
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 99)
public class BeforeActivationInterceptor {

    @Inject
    BeanManager beanManager;

    public static AtomicBoolean isRequestContextActive = new AtomicBoolean(false);

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {

        try {
            isRequestContextActive.set(beanManager.getContext(RequestScoped.class).isActive());
            return ctx.proceed();
        } catch (ContextNotActiveException e) {

        }
        return ctx.proceed();
    }
}

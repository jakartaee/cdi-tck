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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator.beforeBeanDiscovery;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Interceptor
@Priority(2000)
@CustomBinding
public class FooInterceptor {

    public static boolean interceptorInvoked = false;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        interceptorInvoked = true;
        return ctx.proceed();
    }
}

/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@AroundConstructBinding1
@Priority(100)
public class AroundConstructInterceptor1 {
    private static Set<Annotation> allBindings;

    @AroundConstruct
    public Object intercept(InvocationContext ctx) throws Exception {
        allBindings = ctx.getInterceptorBindings();
        return ctx.proceed();
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }
}

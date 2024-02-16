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
package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

public class DogInterceptor {
    private static Set<Annotation> allBindings;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        allBindings = ctx.getInterceptorBindings();
        return "dog: " + ctx.proceed();
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }
}

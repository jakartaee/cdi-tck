/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Priority(Interceptor.Priority.APPLICATION + 400)
@ProductInterceptorBinding2
@Interceptor
public class ProductInterceptor1 {

    private static Set<Annotation> allBindings;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext) throws Exception {
        allBindings = invocationContext.getInterceptorBindings();
        return (1 + (Integer) invocationContext.proceed());
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }

}

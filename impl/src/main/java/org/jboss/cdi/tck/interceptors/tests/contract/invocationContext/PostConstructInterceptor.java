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
package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

@Interceptor
@SimplePCBinding
@Priority(100)
public class PostConstructInterceptor {
    private static boolean getMethodReturnsNull = false;
    private static boolean ctxProceedReturnsNull = false;

    private static Set<Annotation> allBindings = null;

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        getMethodReturnsNull = ctx.getMethod() == null;
        try {
            ctxProceedReturnsNull = ctx.proceed() == null;
        } catch (Exception e) {
        }

        allBindings = ctx.getInterceptorBindings();
    }

    public static boolean isGetMethodReturnsNull() {
        return getMethodReturnsNull;
    }

    public static boolean isCtxProceedReturnsNull() {
        return ctxProceedReturnsNull;
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }
}

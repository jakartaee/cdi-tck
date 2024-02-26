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
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Binding12
@Priority(1200)
public class Interceptor12 {
    private static Set<Annotation> allBindings;

    private static Set<Binding12> binding12s; // must be non-empty
    private static Binding12 binding12; // must be non-null

    private static Set<Binding5> binding5s; // must be empty
    private static Binding6 binding6; // must be null

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        allBindings = ctx.getInterceptorBindings();
        binding12s = ctx.getInterceptorBindings(Binding12.class);
        binding12 = ctx.getInterceptorBinding(Binding12.class);
        binding5s = ctx.getInterceptorBindings(Binding5.class);
        binding6 = ctx.getInterceptorBinding(Binding6.class);
        return ctx.proceed();
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }

    public static Set<Binding12> getBinding12s() {
        return binding12s;
    }

    public static Binding12 getBinding12() {
        return binding12;
    }

    public static Set<Binding5> getBinding5s() {
        return binding5s;
    }

    public static Binding6 getBinding6() {
        return binding6;
    }
}

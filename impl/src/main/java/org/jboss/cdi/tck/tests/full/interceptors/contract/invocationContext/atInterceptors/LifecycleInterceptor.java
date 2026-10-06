/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
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
package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext.atInterceptors;

import jakarta.annotation.PostConstruct;
import jakarta.interceptor.InvocationContext;

public class LifecycleInterceptor {

    private static boolean getMethodReturnsNull = false;
    private static boolean ctxProceedReturnsNull = false;

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        getMethodReturnsNull = ctx.getMethod() == null;
        try {
            ctxProceedReturnsNull = ctx.proceed() == null;
        } catch (Exception e) {
            // ignore
        }
    }

    public static boolean isGetMethodReturnsNull() {
        return getMethodReturnsNull;
    }

    public static boolean isCtxProceedReturnsNull() {
        return ctxProceedReturnsNull;
    }

    public static void reset() {
        getMethodReturnsNull = false;
        ctxProceedReturnsNull = false;
    }
}

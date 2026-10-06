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

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class ContextDataInterceptor1 {

    private static boolean contextDataOK = false;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        ctx.getContextData().put("foo", "bar");
        Object result = ctx.proceed();
        contextDataOK = ctx.getContextData().get("foo").equals("barbar");
        return result;
    }

    public static boolean isContextDataOK() {
        return contextDataOK;
    }

    public static void reset() {
        contextDataOK = false;
    }
}

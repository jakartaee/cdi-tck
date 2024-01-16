/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.cdi.tck.interceptors.tests.order.aroundConstruct;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.cdi.tck.util.ActionSequence;

@Interceptor
@FooClassBinding
@Priority(1)
public class Interceptor1 extends MiddleInterceptor1 {

    @AroundConstruct
    public void intercept3(InvocationContext ctx) {
        ActionSequence.addAction(getClass().getSimpleName());
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.aroundConstruct;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.inject.Inject;
import javax.interceptor.AroundConstruct;
import javax.interceptor.InvocationContext;

public class FooCommonInterceptor {

    @Inject
    public Bar bar;

    public static boolean commonAroundConstructCalled = false;

    // this interceptor method is called first - check whether the dependency injection has been completed
    // on instances of all interceptor classes associated with Foo.class
    @AroundConstruct
    public Object intercept1(InvocationContext ctx) throws Exception {
        assertNotNull(bar);
        assertNotNull(FooSuperInterceptor.bar);
        assertEquals(FooInterceptor.number, 5);
        commonAroundConstructCalled = true;
        return ctx.proceed();
    }

    public static void reset() {
        commonAroundConstructCalled = false;
    }
}

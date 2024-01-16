/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundConstruct;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@BravoBinding
@Priority(Interceptor.Priority.APPLICATION)
public class BravoInterceptor extends AbstractInterceptor {

    public static final String NEW_PARAMETER_VALUE = "enhanced parameter1";

    @AroundConstruct
    public void aroundConstruct(InvocationContext ctx) {
        try {
            setInvoked();

            checkConstructor(ctx, Bravo.class);
            assertNull(ctx.getMethod());
            assertNull(ctx.getTarget());

            // replace constructor parameter
            assertEquals(ctx.getParameters().length, 1);
            assertTrue(ctx.getParameters()[0] instanceof BravoParameter);
            BravoParameter parameter1 = (BravoParameter) ctx.getParameters()[0];
            assertEquals("parameter1", parameter1.getValue());
            ctx.setParameters(new Object[] { new BravoParameter(NEW_PARAMETER_VALUE) });

            assertNull(ctx.proceed());

            checkConstructor(ctx, Bravo.class);
            assertNull(ctx.getMethod());
            assertNotNull(ctx.getTarget());
            assertTrue(ctx.getTarget() instanceof Bravo);

            assertEquals(ctx.getParameters().length, 1);
            assertTrue(ctx.getParameters()[0] instanceof BravoParameter);
            parameter1 = (BravoParameter) ctx.getParameters()[0];
            assertEquals(NEW_PARAMETER_VALUE, parameter1.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

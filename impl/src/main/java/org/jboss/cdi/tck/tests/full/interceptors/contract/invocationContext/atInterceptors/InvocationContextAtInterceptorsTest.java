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

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.CONSTRUCTOR_AND_METHOD_LEVEL_INT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies {@link jakarta.interceptor.InvocationContext} when interceptors are
 * associated using {@link jakarta.interceptor.Interceptors}, complementing the
 * interceptor-binding based coverage in
 * {@code org.jboss.cdi.tck.interceptors.tests.contract.invocationContext}.
 *
 * @see <a href="https://github.com/jakartaee/cdi-tck/issues/405">CDI TCK #405</a>
 */
@SpecVersion(spec = "interceptors", version = "2.2")
@Test(groups = CDI_FULL)
public class InvocationContextAtInterceptorsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InvocationContextAtInterceptorsTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONSTRUCTOR_AND_METHOD_LEVEL_INT, id = "aa")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "c")
    public void testGetTargetMethod() {
        GetTargetInterceptor.reset();
        AtInterceptorsBean instance = getContextualReference(AtInterceptorsBean.class);
        instance.setId(10);
        assertEquals(instance.getId(), 10);
        assertEquals(GetTargetInterceptor.getTarget().getId(), 10);
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "db")
    public void testGetTimerMethod() {
        assertTrue(getContextualReference(AtInterceptorsBean.class).testGetTimer());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ea")
    public void testGetMethodForAroundInvokeInterceptorMethod() {
        assertTrue(getContextualReference(AtInterceptorsBean.class).testGetMethod());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "eb")
    public void testGetMethodForLifecycleCallbackInterceptorMethod() {
        LifecycleInterceptor.reset();
        getContextualReference(AtInterceptorsBean.class);
        assertTrue(LifecycleInterceptor.isGetMethodReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "l")
    public void testCtxProceedForLifecycleCallbackInterceptorMethod() {
        LifecycleInterceptor.reset();
        getContextualReference(AtInterceptorsBean.class);
        assertTrue(LifecycleInterceptor.isCtxProceedReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "f")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ga")
    public void testMethodParameters() {
        assertEquals(getContextualReference(AtInterceptorsBean.class).add(1, 2), 5);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "gb")
    public void testIllegalNumberOfParameters() {
        getContextualReference(AtInterceptorsBean.class).add2(1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "gc")
    public void testIllegalTypeOfParameters() {
        getContextualReference(AtInterceptorsBean.class).add3(1, 1);
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "k")
    public void testProceedReturnsNullForVoidMethod() {
        ProceedReturnsNullInterceptor.reset();
        getContextualReference(AtInterceptorsBean.class).voidMethod();
        assertTrue(ProceedReturnsNullInterceptor.isProceedReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ba")
    public void testContextData() {
        ContextDataInterceptor1.reset();
        ContextDataInterceptor2.reset();
        getContextualReference(AtInterceptorsBean.class).foo();
        assertTrue(ContextDataInterceptor1.isContextDataOK());
        assertTrue(ContextDataInterceptor2.isContextDataOK());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "j")
    public void testBusinessMethodNotCalledWithoutProceedInvocation() {
        AtInterceptorsBean.reset();
        assertEquals(getContextualReference(AtInterceptorsBean.class).echo("foo"), "foo");
        assertFalse(AtInterceptorsBean.isEchoCalled());
    }
}

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
package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the InvocationContext implementation
 *
 * @author Jozef Hartinger
 *
 */
@SpecVersion(spec = "int", version = "1.2")
public class InvocationContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InvocationContextTest.class).build();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = "2.8", id = "aa"), @SpecAssertion(section = "2.3", id = "c")})
    public void testGetTargetMethod() {
        SimpleBean instance = getContextualReference(SimpleBean.class);
        instance.setId(10);
        assertEquals(instance.getId(), 10);
        assertTrue(Interceptor1.isGetTargetOK());
    }

    @Test
    @SpecAssertion(section = "2.3", id = "db")
    public void testGetTimerMethod() {
        assertTrue(getContextualReference(SimpleBean.class).testGetTimer());
    }

    @Test
    @SpecAssertion(section = "2.3", id = "ea")
    public void testGetMethodForAroundInvokeInterceptorMethod() {
        assertTrue(getContextualReference(SimpleBean.class).testGetMethod());
    }

    @Test
    @SpecAssertion(section = "2.3", id = "eb")
    public void testGetMethodForLifecycleCallbackInterceptorMethod() {
        getContextualReference(SimpleBean.class);
        assertTrue(PostConstructInterceptor.isGetMethodReturnsNull());
    }

    @Test
    @SpecAssertion(section = "2.3", id = "l")
    public void testCtxProceedForLifecycleCallbackInterceptorMethod() {
        getContextualReference(SimpleBean.class);
        assertTrue(PostConstructInterceptor.isCtxProceedReturnsNull());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3", id = "f"), @SpecAssertion(section = "2.3", id = "ga") })
    public void testMethodParameters() {
        assertEquals(getContextualReference(SimpleBean.class).add(1, 2), 5);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "2.3", id = "gb")
    public void testIllegalNumberOfParameters() {
        getContextualReference(SimpleBean.class).add2(1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "2.3", id = "gc")
    public void testIllegalTypeOfParameters() {
        getContextualReference(SimpleBean.class).add3(1, 1);
    }

    @Test
    @SpecAssertion(section = "2.3", id = "k")
    public void testProceedReturnsNullForVoidMethod() {
        getContextualReference(SimpleBean.class).voidMethod();
        assertTrue(Interceptor7.isProceedReturnsNull());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3", id = "ba") })
    public void testContextData() {
        getContextualReference(SimpleBean.class).foo();
        assertTrue(Interceptor8.isContextDataOK());
        assertTrue(Interceptor9.isContextDataOK());
    }

    @Test
    @SpecAssertion(section = "2.3", id = "j")
    public void testBusinessMethodNotCalledWithoutProceedInvocation() {
        assert getContextualReference(SimpleBean.class).echo("foo").equals("foo");
        assert !SimpleBean.isEchoCalled();
    }
}

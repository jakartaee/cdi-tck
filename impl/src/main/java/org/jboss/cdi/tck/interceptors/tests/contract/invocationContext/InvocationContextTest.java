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

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.CONSTRUCTOR_AND_METHOD_LEVEL_INT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Tests for the InvocationContext implementation
 *
 * @author Jozef Hartinger
 *
 */
@SpecVersion(spec = "interceptors", version = "2.2")
public class InvocationContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InvocationContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONSTRUCTOR_AND_METHOD_LEVEL_INT, id = "aa")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "c")
    public void testGetTargetMethod() {
        // bean is dependent, contextual ref = contextual instance
        SimpleBean instance = getContextualReference(SimpleBean.class);
        instance.setId(10);
        assertEquals(instance.getId(), 10);
        assertEquals(Interceptor1.getTarget().getId(), 10);
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "db")
    public void testGetTimerMethod() {
        assertTrue(getContextualReference(SimpleBean.class).testGetTimer());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ea")
    public void testGetMethodForAroundInvokeInterceptorMethod() {
        assertTrue(getContextualReference(SimpleBean.class).testGetMethod());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "eb")
    public void testGetMethodForLifecycleCallbackInterceptorMethod() {
        getContextualReference(SimpleBean.class);
        assertTrue(PostConstructInterceptor.isGetMethodReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "l")
    public void testCtxProceedForLifecycleCallbackInterceptorMethod() {
        getContextualReference(SimpleBean.class);
        assertTrue(PostConstructInterceptor.isCtxProceedReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "f")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ga")
    public void testMethodParameters() {
        assertEquals(getContextualReference(SimpleBean.class).add(1, 2), 5);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "gb")
    public void testIllegalNumberOfParameters() {
        getContextualReference(SimpleBean.class).add2(1, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "gc")
    public void testIllegalTypeOfParameters() {
        getContextualReference(SimpleBean.class).add3(1, 1);
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "k")
    public void testProceedReturnsNullForVoidMethod() {
        getContextualReference(SimpleBean.class).voidMethod();
        assertTrue(Interceptor7.isProceedReturnsNull());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ba")
    public void testContextData() {
        getContextualReference(SimpleBean.class).foo();
        assertTrue(Interceptor8.isContextDataOK());
        assertTrue(Interceptor9.isContextDataOK());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "j")
    public void testBusinessMethodNotCalledWithoutProceedInvocation() {
        assertEquals(getContextualReference(SimpleBean.class).echo("foo"), "foo");
        assertFalse(SimpleBean.isEchoCalled());
    }

    @Test
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "n")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "o")
    public void testGetInterceptorBindings() {
        assertTrue(getContextualReference(SimpleBean.class).bindings());
        assertEquals(AroundConstructInterceptor1.getAllBindings(), Set.of(new SimplePCBinding.Literal(),
                new PseudoBinding.Literal(), new AroundConstructBinding1.Literal(),
                new AroundConstructBinding2.Literal()));
        assertEquals(AroundConstructInterceptor1.getAllBindings(), AroundConstructInterceptor2.getAllBindings());
        assertEquals(PostConstructInterceptor.getAllBindings(), Set.of(new SimplePCBinding.Literal(),
                new PseudoBinding.Literal(), new AroundConstructBinding1.Literal()));
        assertEquals(Interceptor12.getAllBindings(), Set.of(new SimplePCBinding.Literal(), new PseudoBinding.Literal(),
                new AroundConstructBinding1.Literal(), new Binding11.Literal(), new Binding12.Literal(),
                new Binding13.Literal("ko"), new Binding14.Literal("foobar"),
                new Binding15.Literal(), new Binding15Additional.Literal("AdditionalBinding")));
        assertEquals(Interceptor12.getBinding12s(), Set.of(new Binding12.Literal()));
        assertEquals(Interceptor12.getBinding12(), new Binding12.Literal());
        assertEquals(Interceptor12.getBinding5s(), Set.of());
        assertNull(Interceptor12.getBinding6());
    }
}

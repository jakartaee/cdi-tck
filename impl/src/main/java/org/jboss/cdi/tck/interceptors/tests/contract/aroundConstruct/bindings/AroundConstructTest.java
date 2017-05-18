/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundConstruct.bindings;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.LIC_EXCEPTIONS;
import static org.jboss.cdi.tck.util.ActionSequence.assertSequenceDataEquals;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 * @author Matus Abaffy
 */
@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundConstructTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AroundConstructTest.class)
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .getOrCreateInterceptors()
                                .clazz(AlphaInterceptor.class.getName(), BravoInterceptor.class.getName(),
                                        CharlieInterceptor1.class.getName(), CharlieInterceptor2.class.getName()).up()).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "c")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "eb")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "f")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "a")
    public void testInterceptorInvocation(Instance<Alpha> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ga")
    public void testReplacingParameters(Instance<Bravo> instance) {
        ActionSequence.reset();
        Bravo bravo = instance.get();
        assertNotNull(bravo.getParameter());
        assertEquals(BravoInterceptor.NEW_PARAMETER_VALUE, bravo.getParameter().getValue());
        assertSequenceDataEquals(BravoInterceptor.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = LIC_EXCEPTIONS, id = "a")
    @SpecAssertion(section = LIC_EXCEPTIONS, id = "b")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "c")
    public void testExceptions(Instance<Charlie> instance) {
        ActionSequence.reset();
        try {
            instance.get();
            fail();
        } catch (CharlieException e) {
            // reverse order because interceptors are stored in the ActionSequence after ctx.proceed() returns
            assertSequenceDataEquals(CharlieInterceptor2.class, CharlieInterceptor1.class);
        } catch (Throwable e) {
            fail();
        }
    }
}

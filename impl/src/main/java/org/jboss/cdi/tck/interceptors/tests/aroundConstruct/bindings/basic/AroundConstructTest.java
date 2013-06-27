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
package org.jboss.cdi.tck.interceptors.tests.aroundConstruct.bindings.basic;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "int", version = "1.2")
public class AroundConstructTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AroundConstructTest.class)
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .createInterceptors()
                                .clazz(AlphaInterceptor.class.getName(), BravoInterceptor.class.getName(),
                                        CharlieInterceptor1.class.getName(), CharlieInterceptor2.class.getName()).up()).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "2.3", id = "c"), @SpecAssertion(section = "2.3", id = "eb"),
        @SpecAssertion(section = "2.3", id = "f"), @SpecAssertion(section = "2.6", id = "a"), })
    public void testInterceptorInvocation(Instance<Alpha> instance) {
        AlphaInterceptor.reset();
        instance.get();
        assertTrue(AlphaInterceptor.isInvoked());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "2.3", id = "ga"), })
    public void testReplacingParameters(Instance<Bravo> instance) {
        BravoInterceptor.reset();
        Bravo bravo = instance.get();
        assertNotNull(bravo.getParameter());
        assertEquals(BravoInterceptor.NEW_PARAMETER_VALUE, bravo.getParameter().getValue());
        assertTrue(BravoInterceptor.isInvoked());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "2.6.1", id = "a"), @SpecAssertion(section = "2.6.1", id = "b")})
    public void testExceptions(Instance<Charlie> instance) {
        CharlieInterceptor1.reset();
        CharlieInterceptor2.reset();
        try {
            instance.get();
            fail();
        } catch (CharlieException e) {
            assertTrue(CharlieInterceptor1.isInvoked());
            assertTrue(CharlieInterceptor2.isInvoked());
        } catch (Throwable e) {
            fail();
        }
    }

    // FIXME remove?
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testSerialization(Instance<Alpha> instance) throws Exception {
        activate(passivate(instance.get()));
    }
}

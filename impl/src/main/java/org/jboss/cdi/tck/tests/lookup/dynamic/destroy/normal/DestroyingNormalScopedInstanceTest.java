/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.dynamic.destroy.normal;

import static org.jboss.cdi.tck.cdi.Sections.DYNAMIC_LOOKUP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for https://issues.jboss.org/browse/CDI-139
 * 
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DestroyingNormalScopedInstanceTest extends AbstractTest {

    private static final String[] VALUES = { "foo", "bar", "baz" };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyingNormalScopedInstanceTest.class)
                .withExtension(CustomScopeExtension.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "o") })
    public void testApplicationScopedComponent(Instance<ApplicationScopedComponent> instance) {
        testComponent(instance);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "o") })
    public void testRequestScopedComponent(Instance<RequestScopedComponent> instance) {
        testComponent(instance);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "o") })
    public void testCustomScopedComponent(Instance<CustomScopedComponent> instance) {
        testComponent(instance);
    }

    /**
     * TODO add assertion - OPEN ISSUE atm
     * 
     * @param application
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testNothingHappensIfNoInstanceToDestroy(ApplicationScopedComponent application) {
        Bean<?> bean = getUniqueBean(ApplicationScopedComponent.class);
        AlterableContext context = (AlterableContext) getCurrentManager().getContext(bean.getScope());

        AbstractComponent.reset();
        application.setValue("value");
        context.destroy(bean);
        assertTrue(AbstractComponent.isDestroyed());

        context.destroy(bean); // make sure subsequent calls do not raise exception
        context.destroy(bean);
    }

    private <T extends AbstractComponent> void testComponent(Instance<T> instance) {
        for (String string : VALUES) {
            T reference = instance.get();
            assertNull(reference.getValue());
            reference.setValue(string);
            assertEquals(reference.getValue(), string);

            AbstractComponent.reset();
            instance.destroy(reference);
            assertTrue(AbstractComponent.isDestroyed());
            assertNull(reference.getValue(), reference.getValue());
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "e") })
    public void testUnsupportedOperationExceptionThrownIfUnderlyingContextNotAlterable(
            Instance<CustomScopedComponent> instance, CustomScopeExtension extension) {
        try {
            extension.switchToNonAlterable();
            CustomScopedComponent component = instance.get();
            instance.destroy(component);
            fail("expected exception not thrown");
        } catch (UnsupportedOperationException expected) {
        } finally {
            extension.switchToAlterable();
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "o") })
    public void testContextDestroyCalled(Instance<CustomScopedComponent> instance) {
        CustomScopedComponent component = instance.get();
        CustomAlterableContext.reset();
        instance.destroy(component);
        assertTrue(CustomAlterableContext.isDestroyCalled());
    }

    /**
     * TODO add assertion - OPEN ISSUE atm
     * 
     * @param application
     */

    @Test(expectedExceptions = NullPointerException.class, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "o") })
    public void testNullParameter(Instance<ApplicationScopedComponent> instance) {
        instance.destroy(null);
    }
}

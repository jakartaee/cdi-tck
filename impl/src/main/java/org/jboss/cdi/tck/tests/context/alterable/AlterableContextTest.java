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
package org.jboss.cdi.tck.tests.context.alterable;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
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
@SpecVersion(spec = "cdi", version = "2.0")
public class AlterableContextTest extends AbstractTest {

    private static final String[] VALUES = { "foo", "bar", "baz" };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AlterableContextTest.class)
                .withExtension(CustomScopeExtension.class).build();
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "t")
    public void testApplicationScopedComponent() {
        testComponent(ApplicationScopedComponent.class);
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "t")
    public void testRequestScopedComponent() {
        testComponent(RequestScopedComponent.class);
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "t")
    public void testCustomScopedComponent() {
        testComponent(CustomScopedComponent.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = CONTEXT, id = "t")
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

    private <T extends AbstractComponent> void testComponent(Class<T> javaClass) {
        Bean<?> bean = getUniqueBean(javaClass);
        @SuppressWarnings("unchecked")
        T reference = (T) getCurrentManager().getReference(bean, javaClass, getCurrentManager().createCreationalContext(bean));
        AlterableContext context = (AlterableContext) getCurrentManager().getContext(bean.getScope());

        for (String string : VALUES) {
            assertNull(reference.getValue());
            reference.setValue(string);
            assertEquals(reference.getValue(), string);

            AbstractComponent.reset();
            context.destroy(bean);
            assertTrue(AbstractComponent.isDestroyed());
            assertNull(reference.getValue(), reference.getValue());
        }
    }
}

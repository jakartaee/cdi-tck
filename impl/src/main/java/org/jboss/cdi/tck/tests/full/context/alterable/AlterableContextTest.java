/*
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
package org.jboss.cdi.tck.tests.full.context.alterable;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for https://issues.jboss.org/browse/CDI-139
 * 
 * <p>
 * This test was originally part of the Weld test suite.
 * </p>
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
                .withExtension(CustomScopeExtension.class).withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test(groups = CDI_FULL)
    @SpecAssertion(section = CONTEXT, id = "t")
    public void testCustomScopedComponent() {
        testComponent(CustomScopedComponent.class);
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

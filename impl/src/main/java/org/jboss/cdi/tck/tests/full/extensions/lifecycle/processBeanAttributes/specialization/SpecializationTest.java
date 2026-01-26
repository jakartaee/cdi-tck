/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.specialization;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationsMatch;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class SpecializationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(SpecializationTest.class)
                .withClasses(Alpha.class, Bar.class, Baz.class, Bravo.class, Foo.class, Charlie.class, VerifyingExtension.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtension(VerifyingExtension.class).build();
    }

    @Inject
    private VerifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa"),
            @SpecAssertion(section = SPECIALIZATION, id = "ca") })
    public void testProcessBeanAttributesFiredProperlyForSpecializedBean() {
        assertNull(extension.getAlpha());
        assertNull(extension.getBravo());
        BeanAttributes<Charlie> charlieAttributes = extension.getCharlie();
        assertNotNull(charlieAttributes);
        assertAnnotationsMatch(charlieAttributes.getQualifiers(), Foo.Literal.INSTANCE, Bar.Literal.INSTANCE,
                Baz.Literal.INSTANCE, Any.Literal.INSTANCE, NamedLiteral.of("alpha"));
        assertEquals(charlieAttributes.getName(), "alpha");
    }

}

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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.specialization;

import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.literal.NamedLiteral;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class VetoTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(VetoTest.class)
                .withClasses(Alpha.class, Bar.class, Baz.class, Bravo.class, Foo.class, Charlie.class, VetoingExtension.class, VerifyingExtension.class)
                .withExtensions(VetoingExtension.class, VerifyingExtension.class).build();
    }

    @Inject
    @Any
    Alpha alpha;
    
    @Inject
    private VerifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "be"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa"),
            @SpecAssertion(section = SPECIALIZATION, id = "ca") })
    public void testSpecializedBeanAvailableAfterSpecializingBeanVetoed() {
        Bean<Alpha> bean = getUniqueBean(Alpha.class, Any.Literal.INSTANCE);
        assertNotNull(bean);
        assertEquals(bean.getBeanClass(), Bravo.class);
        assertEquals(bean.getName(), "alpha");
        assertTrue(annotationSetMatches(bean.getQualifiers(), Foo.Literal.INSTANCE, Bar.Literal.INSTANCE, NamedLiteral.of(
                "alpha"), Any.Literal.INSTANCE));
        assertNotNull(alpha);
        assertTrue(alpha instanceof Bravo);
        assertFalse(alpha instanceof Charlie);
        assertNull(extension.getAlpha());
        assertNotNull(extension.getBravo());
        assertNotNull(extension.getCharlie());
    }
}

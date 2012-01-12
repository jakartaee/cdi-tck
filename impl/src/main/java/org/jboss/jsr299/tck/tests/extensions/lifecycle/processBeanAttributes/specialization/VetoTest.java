/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.extensions.lifecycle.processBeanAttributes.specialization;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.NamedLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class VetoTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(VetoTest.class)
                .withClasses(Alpha.class, Bar.class, Baz.class, Bravo.class, Foo.class, Charlie.class, VetoingExtension.class)
                .withExtension(VetoingExtension.class).build();
    }

    @Inject
    @Any
    Alpha alpha;
    
    @Inject
    private VerifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "be"), @SpecAssertion(section = "11.5.10", id = "aa"),
            @SpecAssertion(section = "4.3", id = "ca") })
    public void testSpecializedBeanAvailableAfterSpecializingBeanVetoed() {
        Bean<Alpha> bean = getUniqueBean(Alpha.class, AnyLiteral.INSTANCE);
        assertNotNull(bean);
        assertEquals(bean.getBeanClass(), Bravo.class);
        assertEquals(bean.getName(), "alpha");
        assertTrue(annotationSetMatches(bean.getQualifiers(), Foo.Literal.INSTANCE, Bar.Literal.INSTANCE, new NamedLiteral(
                "alpha"), AnyLiteral.INSTANCE));
        assertNotNull(alpha);
        assertTrue(alpha instanceof Bravo);
        assertFalse(alpha instanceof Charlie);
        assertNull(extension.getAlpha());
        assertNotNull(extension.getBravo());
        assertNotNull(extension.getCharlie());
    }
}

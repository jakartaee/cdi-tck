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
package org.jboss.jsr299.tck.tests.implementation.builtin.servlet;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class ServletContainerBuiltinBeanTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ServletContainerBuiltinBeanTest.class).build();
    }

    @Inject
    LowercaseConverter lowercaseConverter;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "da"), @SpecAssertion(section = "3.7", id = "db"),
            @SpecAssertion(section = "3.7", id = "dc") })
    public void testBuiltinBeans() {
        String result = lowercaseConverter.convert("Foo");
        assertEquals(result, "foo");
        assertNotNull(lowercaseConverter.getHttpServletRequest());
        assertNotNull(lowercaseConverter.getHttpServletRequest().getRequestURL());
        assertNotNull(lowercaseConverter.getHttpSession());
        assertNotNull(lowercaseConverter.getHttpSession().getId());
        assertNotNull(lowercaseConverter.getServletContext());
        assertTrue(lowercaseConverter.getServletContext().getMajorVersion() >= 2);
    }

}

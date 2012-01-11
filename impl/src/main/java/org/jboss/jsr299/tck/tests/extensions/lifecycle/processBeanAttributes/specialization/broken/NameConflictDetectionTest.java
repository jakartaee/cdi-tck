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
package org.jboss.jsr299.tck.tests.extensions.lifecycle.processBeanAttributes.specialization.broken;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;

import javax.enterprise.inject.spi.ProcessBeanAttributes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.util.ForwardingBeanAttributes;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.testng.annotations.Test;

/**
 * 
 * The spec says: "If Y has a name and X declares a name explicitly, using @Named, the container automatically detects the
 * problem and treats it as a definition error."
 * 
 * This test verifies that the container detects this error even if the name is added to Y in a {@link ProcessBeanAttributes}
 * observer method.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
public class NameConflictDetectionTest extends AbstractJSR299Test {

    @ShouldThrowException(Exception.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(NameConflictDetectionTest.class)
                .withClasses(Specialized.class, Specializing.class, NameExtension.class, ForwardingBeanAttributes.class)
                .withExtension(NameExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa"), @SpecAssertion(section = "4.3.1", id = "lb") })
    public void testDeployment() {
    }
}

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
package org.jboss.jsr299.tck.tests.veto;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.jsr299.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.jsr299.tck.tests.veto.aquarium.Piranha;
import org.jboss.jsr299.tck.tests.veto.pen.Giraffe;
import org.jboss.jsr299.tck.tests.veto.pen.Rhino;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * Temporarily marked as integration tests - see SHRINKWRAP-369.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class VetoTest extends AbstractJSR299Test {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(VetoTest.class)
                .withClasses(AnnotatedTypeWrapper.class, AnnotatedWrapper.class)
                .withExtensions(ModifyingExtension.class, VerifyingExtension.class).build()
                .addPackage(Piranha.class.getPackage()).addPackage(Giraffe.class.getPackage());
    }

    @Inject
    private VerifyingExtension verifyingExtension;

    @Test
    @SpecAssertion(section = "3.12", id = "a")
    public void testClassLevelVeto() {
        assertTrue(verifyingExtension.getClasses().contains(Elephant.class));
        assertEquals(getCurrentManager().getBeans(Elephant.class, AnyLiteral.INSTANCE).size(), 0);
    }

    @Test
    @SpecAssertion(section = "3.12", id = "a")
    public void testPackageLevelVeto() {
        assertTrue(verifyingExtension.getClasses().contains(Piranha.class));
        assertEquals(getCurrentManager().getBeans(Piranha.class, AnyLiteral.INSTANCE).size(), 0);
    }

    @Test
    @SpecAssertion(section = "3.12", id = "b")
    public void testClassLevelRequirements() {
        assertTrue(verifyingExtension.getClasses().contains(Lion.class));
        assertTrue(verifyingExtension.getClasses().contains(Tiger.class));
        assertEquals(getCurrentManager().getBeans(Lion.class, AnyLiteral.INSTANCE).size(), 1);
        assertEquals(getCurrentManager().getBeans(Tiger.class, AnyLiteral.INSTANCE).size(), 0);
    }

    @Test
    @SpecAssertion(section = "3.12", id = "b")
    public void testPackageLevelRequirements() {
        assertTrue(verifyingExtension.getClasses().contains(Rhino.class));
        assertTrue(verifyingExtension.getClasses().contains(Giraffe.class));
        assertEquals(getCurrentManager().getBeans(Rhino.class, AnyLiteral.INSTANCE).size(), 0);
        assertEquals(getCurrentManager().getBeans(Giraffe.class, AnyLiteral.INSTANCE).size(), 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.12", id = "a"), @SpecAssertion(section = "3.12", id = "b") })
    public void testReplacingAnnotatedTypeWithExtension() {
        assertTrue(verifyingExtension.getClasses().contains(Leopard.class));
        assertEquals(getCurrentManager().getBeans(Leopard.class, AnyLiteral.INSTANCE).size(), 1);
    }
}

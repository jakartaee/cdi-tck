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
package org.jboss.cdi.tck.tests.veto;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.cdi.tck.tests.veto.aquarium.Piranha;
import org.jboss.cdi.tck.tests.veto.pen.Giraffe;
import org.jboss.cdi.tck.tests.veto.pen.Rhino;
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
public class VetoTest extends AbstractTest {

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

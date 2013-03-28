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
package org.jboss.cdi.tck.tests.vetoed;

import static org.jboss.cdi.tck.cdi.Sections.PAT;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Fish;
import org.jboss.cdi.tck.tests.vetoed.aquarium.FishType;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Fishy;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Piranha;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class VetoedTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(VetoedTest.class)
                .withClasses(Animal.class, Elephant.class, Shark.class, Predator.class, MissileBinding.class,
                        DummyScoped.class, AnimalStereotype.class, Tiger.class, ModifyingExtension.class,
                        VerifyingExtension.class, Shark.class).withPackage(Piranha.class.getPackage())
                .withExtensions(ModifyingExtension.class, VerifyingExtension.class).withLibrary(Gecko.class, Reptile.class)
                .build();
    }

    @Inject
    VerifyingExtension verifyingExtension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h"),
            @SpecAssertion(section = PAT, id = "ia"), @SpecAssertion(section = PAT, id = "ib") })
    public void testClassLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Elephant.class));
        assertEquals(getCurrentManager().getBeans(Elephant.class, AnyLiteral.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Animal.class));
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h"),
            @SpecAssertion(section = PAT, id = "ii") })
    public void testPackageLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Piranha.class));
        assertFalse(verifyingExtension.getClasses().contains(Fish.class));
        assertFalse(verifyingExtension.getClasses().contains(FishType.class));
        assertFalse(verifyingExtension.getClasses().contains(Fishy.class));
        assertTrue(verifyingExtension.getClasses().contains(Shark.class));
        assertEquals(getCurrentManager().getBeans(Piranha.class, AnyLiteral.INSTANCE).size(), 0);
        assertEquals(getCurrentManager().getBeans(Shark.class, AnyLiteral.INSTANCE).size(), 1);
        assertEquals(getCurrentManager().getBeans(Shark.class, new AnnotationLiteral<Fishy>() {
        }).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PAT, id = "ie"),
            @SpecAssertion(section = PAT, id = "if") })
    public void testAnnotatedTypeAddedByExtension() {
        assertFalse(verifyingExtension.getClasses().contains(Gecko.class));
        assertEquals(getCurrentManager().getBeans(Gecko.class, AnyLiteral.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Reptile.class));
    }

}

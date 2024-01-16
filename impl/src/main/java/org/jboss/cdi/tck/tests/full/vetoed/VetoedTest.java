/*
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
package org.jboss.cdi.tck.tests.full.vetoed;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.vetoed.aquarium.Fish;
import org.jboss.cdi.tck.tests.full.vetoed.aquarium.FishType;
import org.jboss.cdi.tck.tests.full.vetoed.aquarium.Fishy;
import org.jboss.cdi.tck.tests.full.vetoed.aquarium.Piranha;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class VetoedTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(VetoedTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withClasses(Animal.class, Elephant.class, Shark.class, Predator.class,
                        AnimalStereotype.class, Tiger.class, ModifyingExtension.class,
                        VerifyingExtension.class, Shark.class).withPackage(Piranha.class.getPackage())
                .withExtensions(ModifyingExtension.class, VerifyingExtension.class)
                .withLibrary(new BeansXml(BeanDiscoveryMode.ALL), false, Gecko.class, Reptile.class)
                .build();
    }

    @Inject
    VerifyingExtension verifyingExtension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ia"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ib") })
    public void testClassLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Elephant.class));
        assertEquals(getCurrentManager().getBeans(Elephant.class, Any.Literal.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Animal.class));
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ii") })
    public void testPackageLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Piranha.class));
        assertFalse(verifyingExtension.getClasses().contains(Fish.class));
        assertFalse(verifyingExtension.getClasses().contains(FishType.class));
        assertFalse(verifyingExtension.getClasses().contains(Fishy.class));
        assertTrue(verifyingExtension.getClasses().contains(Shark.class));
        assertEquals(getCurrentManager().getBeans(Piranha.class, Any.Literal.INSTANCE).size(), 0);
        assertEquals(getCurrentManager().getBeans(Shark.class, Any.Literal.INSTANCE).size(), 1);
        assertEquals(getCurrentManager().getBeans(Shark.class, new Fishy.Literal()).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ie"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "if") })
    public void testAnnotatedTypeAddedByExtension() {
        assertFalse(verifyingExtension.getClasses().contains(Gecko.class));
        assertEquals(getCurrentManager().getBeans(Gecko.class, Any.Literal.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Reptile.class));
    }

}

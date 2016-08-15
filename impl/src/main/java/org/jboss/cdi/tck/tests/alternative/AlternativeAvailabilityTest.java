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
package org.jboss.cdi.tck.tests.alternative;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_ALTERNATIVE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.INTER_MODULE_INJECTION;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class AlternativeAvailabilityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AlternativeAvailabilityTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives()
                                .clazz(Chicken.class.getName(), EnabledSheepProducer.class.getName(), SnakeProducer.class.getName())
                                .stereotype(EnabledAlternativeStereotype.class.getName()).up()).build();
    }

    @SuppressWarnings("serial")
    private static final AnnotationLiteral<Wild> WILD_LITERAL = new AnnotationLiteral<Wild>() {
    };

    @SuppressWarnings("serial")
    private static final AnnotationLiteral<Tame> TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Test
    @SpecAssertions({ @SpecAssertion(section = SELECTION, id = "e"),
            @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "ba"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "aa"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "g")

    })
    public void testAlternativeAvailability() throws Exception {

        // Bird, Cat and Chicken = enabled alternatives
        // Dog and Horse = not enabled alternatives

        Set<Bean<Animal>> animals = getBeans(Animal.class);
        Set<Type> types = new HashSet<Type>();
        for (Bean<Animal> animal : animals) {
            types.addAll(animal.getTypes());
        }

        assertTrue(types.contains(Chicken.class));
        assertTrue(types.contains(Cat.class));
        assertTrue(types.contains(Bird.class));
        assertFalse(types.contains(Horse.class));
        assertFalse(types.contains(Dog.class));

        assertEquals(getCurrentManager().getBeans("cat").size(), 1);
        assertEquals(getCurrentManager().getBeans("dog").size(), 0);
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bc")
    public void testIsAlternative() {
        Bean<?> cat = getCurrentManager().resolve(getCurrentManager().getBeans(Cat.class));
        assertTrue(cat.isAlternative());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "d"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "ba"), @SpecAssertion(section = STEREOTYPES, id = "aa"),
            @SpecAssertion(section = ALTERNATIVE_STEREOTYPE, id = "a") })
    public void testAnyEnabledAlternativeStereotypeMakesAlternativeEnabled() throws Exception {
        assertEquals(getBeans(Bird.class).size(), 1);
        assertEquals(getCurrentManager().getBeans("bird").size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INTER_MODULE_INJECTION, id = "e"),
            @SpecAssertion(section = INTER_MODULE_INJECTION, id = "f"),
            @SpecAssertion(section = INTER_MODULE_INJECTION, id = "g"),
            @SpecAssertion(section = INTER_MODULE_INJECTION, id = "h") })
    public void testProducersOnAlternativeClass() throws Exception {
        assertEquals(getBeans(Sheep.class, WILD_LITERAL).size(), 2);
        assertEquals(getBeans(Sheep.class, TAME_LITERAL).size(), 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "ca"),
            @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "cb"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "ab"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "ac") })
    public void testProducerAlternativesOnMethodAndField() throws Exception {
        assertEquals(getBeans(Cat.class, WILD_LITERAL).size(), 2);
        assertEquals(getBeans(Cat.class, TAME_LITERAL).size(), 0);
        Set<Bean<?>> snakeBeans = getCurrentManager().getBeans(Snake.class, WILD_LITERAL);
        assertNotNull(getCurrentManager().resolve(snakeBeans));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "d"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "bb"),
            @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "bc") })
    public void testStereotypeAlternativeOnProducerMethodAndField() throws Exception {
        assertEquals(getBeans(Bird.class, WILD_LITERAL).size(), 0);
        assertEquals(getBeans(Bird.class, TAME_LITERAL).size(), 2);
    }

}

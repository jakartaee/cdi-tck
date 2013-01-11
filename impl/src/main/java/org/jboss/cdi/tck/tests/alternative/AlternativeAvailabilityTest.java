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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class AlternativeAvailabilityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AlternativeAvailabilityTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createAlternatives()
                                .clazz(Chicken.class.getName(), EnabledSheepProducer.class.getName())
                                .stereotype(EnabledAlternativeStereotype.class.getName()).up()).build();
    }

    @SuppressWarnings("serial")
    private static final AnnotationLiteral<Wild> WILD_LITERAL = new AnnotationLiteral<Wild>() {
    };

    @SuppressWarnings("serial")
    private static final AnnotationLiteral<Tame> TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.1", id = "e"), @SpecAssertion(section = "5.1.1", id = "ab"),
            @SpecAssertion(section = "5.1.1", id = "ca"), @SpecAssertion(section = "2.6", id = "a"),
            @SpecAssertion(section = "2.6.1", id = "a"), @SpecAssertion(section = "12.4", id = "ka")

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
    @SpecAssertion(section = "11.1", id = "bc")
    public void testIsAlternative() {
        Bean<?> cat = getCurrentManager().resolve(getCurrentManager().getBeans(Cat.class));
        assertTrue(cat.isAlternative());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.1.1", id = "cf"), @SpecAssertion(section = "2.6.1", id = "b"),
            @SpecAssertion(section = "2.7", id = "aa"), @SpecAssertion(section = "2.7.1.4", id = "a") })
    public void testAnyEnabledAlternativeStereotypeMakesAlternativeEnabled() throws Exception {
        assertEquals(getBeans(Bird.class).size(), 1);
        assertEquals(getCurrentManager().getBeans("bird").size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.1.1", id = "cc"), @SpecAssertion(section = "5.1.1", id = "cd") })
    public void testProducersOnAlternativeClass() throws Exception {
        assertEquals(getBeans(Sheep.class, WILD_LITERAL).size(), 2);
        assertEquals(getBeans(Sheep.class, TAME_LITERAL).size(), 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.6.1", id = "ab"), @SpecAssertion(section = "2.6.1", id = "ac") })
    public void testProducerAlternativesOnMethodAndField() throws Exception {
        assertEquals(getBeans(Cat.class, WILD_LITERAL).size(), 2);
        assertEquals(getBeans(Cat.class, TAME_LITERAL).size(), 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.6.1", id = "c"), @SpecAssertion(section = "2.6.1", id = "d") })
    public void testStereotypeAlternativeOnProducerMethodAndField() throws Exception {
        assertEquals(getBeans(Bird.class, WILD_LITERAL).size(), 0);
        assertEquals(getBeans(Bird.class, TAME_LITERAL).size(), 2);
    }

}

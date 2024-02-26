/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.INJECTED_FIELD_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.MULTIPLE_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PRIMITIVE_TYPES_AND_NULL_VALUES;
import static org.jboss.cdi.tck.cdi.Sections.QUALIFIER_ANNOTATION_MEMBERS;
import static org.jboss.cdi.tck.cdi.Sections.RESTRICTING_BEAN_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "2.0")
public class ResolutionByTypeTest extends AbstractTest {

    private static final TypeLiteral<FlightlessBird<Australian>> AUSTRALIAN_FLIGHTLESS_BIRD = new TypeLiteral<FlightlessBird<Australian>>() {
    };
    private static final TypeLiteral<FlightlessBird<European>> EUROPEAN_FLIGHTLESS_BIRD = new TypeLiteral<FlightlessBird<European>>() {
    };
    private static final TypeLiteral<Cat<European>> EUROPEAN_CAT = new TypeLiteral<Cat<European>>() {
    };
    private static final TypeLiteral<Cat<African>> AFRICAN_CAT = new TypeLiteral<Cat<African>>() {
    };
    private static final Annotation TAME = new Tame.Literal();
    private static final Annotation WILD = new Wild.Literal();
    private static final Annotation NUMBER = new Number.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolutionByTypeTest.class).build();
    }

    @Test
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "lb")
    public void testDefaultBindingTypeAssumed() throws Exception {
        Set<Bean<Tuna>> possibleTargets = getBeans(Tuna.class);
        assertEquals(possibleTargets.size(), 1);
        assertTrue(possibleTargets.iterator().next().getTypes().contains(Tuna.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTED_FIELD_QUALIFIERS, id = "b"),
            @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "lc"),
            @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "d"),
            @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "la"),
            @SpecAssertion(section = MULTIPLE_QUALIFIERS, id = "a"), @SpecAssertion(section = MULTIPLE_QUALIFIERS, id = "d") })
    public void testAllQualifiersSpecifiedForResolutionMustAppearOnBean() {

        Set<Bean<Animal>> animalBeans = getBeans(Animal.class, new ChunkyLiteral(), new Whitefish.Literal());
        assertEquals(animalBeans.size(), 1);
        assertTrue(animalBeans.iterator().next().getTypes().contains(Cod.class));

        Set<Bean<ScottishFish>> scottishFishBeans = getBeans(ScottishFish.class, new Whitefish.Literal());
        assertEquals(scottishFishBeans.size(), 2);

        for (Bean<ScottishFish> bean : scottishFishBeans) {
            if (!bean.getTypes().contains(Cod.class) && !bean.getTypes().contains(Sole.class)) {
                fail();
            }
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "ka") })
    public void testResolveByTypeWithTypeParameter() throws Exception {

        Set<Bean<Farmer<ScottishFish>>> beans = getBeans(new TypeLiteral<Farmer<ScottishFish>>() {
        });
        assertEquals(beans.size(), 1);
        assertTrue(beans.iterator().next().getTypes().contains(ScottishFishFarmer.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "j"),
            @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "i") })
    public void testResolveByTypeWithArray() throws Exception {
        assertEquals(getBeans(Spider[].class).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "i"),
            @SpecAssertion(section = PRIMITIVE_TYPES_AND_NULL_VALUES, id = "aa"),
            @SpecAssertion(section = PRIMITIVE_TYPES_AND_NULL_VALUES, id = "ab"),
            @SpecAssertion(section = MULTIPLE_QUALIFIERS, id = "b"),
            @SpecAssertion(section = MULTIPLE_QUALIFIERS, id = "c"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "j") })
    public void testResolveByTypeWithPrimitives() {

        assertEquals(getBeans(Double.class, NUMBER).size(), 2);
        assertEquals(getBeans(double.class, NUMBER).size(), 2);

        Double min = getContextualReference(Double.class, new Min.Literal());
        double max = getContextualReference(double.class, new Max.Literal());

        assertEquals(min, Double.valueOf(NumberProducer.min));
        assertEquals(Double.valueOf(max), NumberProducer.max);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "ld"),
            @SpecAssertion(section = QUALIFIER_ANNOTATION_MEMBERS, id = "b") })
    public void testResolveByTypeWithNonBindingMembers() throws Exception {

        Set<Bean<Animal>> beans = getBeans(Animal.class, new ExpensiveLiteral() {
            public int cost() {
                return 60;
            }

            public boolean veryExpensive() {
                return true;
            }

        }, new Whitefish.Literal());
        assertEquals(beans.size(), 2);

        Set<Type> classes = new HashSet<Type>();
        for (Bean<Animal> bean : beans) {
            classes.addAll(bean.getTypes());
        }
        assertTrue(classes.contains(Halibut.class));
        assertTrue(classes.contains(RoundWhitefish.class));
        assertFalse(classes.contains(Sole.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "a")
    public void testBeanTypesOnManagedBean() {
        assertEquals(getBeans(Canary.class).size(), 1);
        Bean<Canary> bean = getUniqueBean(Canary.class);
        assertTrue(getBeans(Bird.class).isEmpty());
        assertTrue(typeSetMatches(bean.getTypes(), Canary.class, Object.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "e")
    public void testGenericBeanTypesOnManagedBean() {
        assertEquals(getBeans(AUSTRALIAN_FLIGHTLESS_BIRD).size(), 1);
        assertTrue(getBeans(Emu.class).isEmpty());
        assertTrue(getBeans(EUROPEAN_FLIGHTLESS_BIRD).isEmpty());
        Bean<FlightlessBird<Australian>> bean = getUniqueBean(AUSTRALIAN_FLIGHTLESS_BIRD);
        assertTrue(typeSetMatches(bean.getTypes(), AUSTRALIAN_FLIGHTLESS_BIRD.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "c")
    public void testBeanTypesOnProducerMethod() {
        assertEquals(getBeans(Parrot.class).size(), 1);
        assertTrue(getBeans(Bird.class).isEmpty());
        Bean<Parrot> bean = getUniqueBean(Parrot.class);
        assertTrue(typeSetMatches(bean.getTypes(), Parrot.class, Object.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "h")
    public void testGenericBeanTypesOnProducerField() {
        assertEquals(getBeans(EUROPEAN_CAT, TAME).size(), 1);
        assertTrue(getBeans(DomesticCat.class, TAME).isEmpty());
        Bean<Cat<European>> bean = getUniqueBean(EUROPEAN_CAT, TAME);
        assertTrue(typeSetMatches(bean.getTypes(), EUROPEAN_CAT.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "g")
    public void testGenericBeanTypesOnProducerMethod() {
        assertEquals(getBeans(AFRICAN_CAT, WILD).size(), 1);
        assertTrue(getBeans(Lion.class, WILD).isEmpty());
        Bean<Cat<African>> bean = getUniqueBean(AFRICAN_CAT, WILD);
        assertTrue(typeSetMatches(bean.getTypes(), AFRICAN_CAT.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = RESTRICTING_BEAN_TYPES, id = "d")
    public void testBeanTypesOnProducerField() {
        assertEquals(getBeans(Dove.class).size(), 1);
        assertTrue(getBeans(Bird.class).isEmpty());
        Bean<Dove> bean = getUniqueBean(Dove.class);
        assertTrue(typeSetMatches(bean.getTypes(), Dove.class, Object.class));
    }
}

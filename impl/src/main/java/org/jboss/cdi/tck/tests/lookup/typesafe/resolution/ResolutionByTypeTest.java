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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "20091101")
public class ResolutionByTypeTest extends AbstractTest {

    private static final TypeLiteral<FlightlessBird<Australian>> AUSTRALIAN_FLIGHTLESS_BIRD = new TypeLiteral<FlightlessBird<Australian>>() {
    };
    private static final TypeLiteral<FlightlessBird<European>> EUROPEAN_FLIGHTLESS_BIRD = new TypeLiteral<FlightlessBird<European>>() {
    };
    private static final TypeLiteral<Cat<European>> EUROPEAN_CAT = new TypeLiteral<Cat<European>>() {
    };
    private static final TypeLiteral<Cat<African>> AFRICAN_CAT = new TypeLiteral<Cat<African>>() {
    };
    private static final Annotation TAME = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation WILD = new AnnotationLiteral<Wild>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolutionByTypeTest.class).build();
    }

    @Test
    @SpecAssertion(section = "5.2.1", id = "lb")
    public void testDefaultBindingTypeAssumed() throws Exception {
        Set<Bean<Tuna>> possibleTargets = getBeans(Tuna.class);
        assertEquals(possibleTargets.size(), 1);
        assertTrue(possibleTargets.iterator().next().getTypes().contains(Tuna.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3.4", id = "b"), @SpecAssertion(section = "5.2.1", id = "lc"),
            @SpecAssertion(section = "2.3.3", id = "d"), @SpecAssertion(section = "5.2.1", id = "la"),
            @SpecAssertion(section = "5.2.7", id = "a"), @SpecAssertion(section = "5.2.7", id = "d") })
    public void testAllQualifiersSpecifiedForResolutionMustAppearOnBean() {

        Set<Bean<Animal>> animalBeans = getBeans(Animal.class, new ChunkyLiteral(), new AnnotationLiteral<Whitefish>() {
        });
        assertEquals(animalBeans.size(), 1);
        assertTrue(animalBeans.iterator().next().getTypes().contains(Cod.class));

        Set<Bean<ScottishFish>> scottishFishBeans = getBeans(ScottishFish.class, new AnnotationLiteral<Whitefish>() {
        });
        assertEquals(scottishFishBeans.size(), 2);

        for (Bean<ScottishFish> bean : scottishFishBeans) {
            if (!bean.getTypes().contains(Cod.class) && !bean.getTypes().contains(Sole.class)) {
                fail();
            }
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "ka") })
    public void testResolveByTypeWithTypeParameter() throws Exception {

        Set<Bean<Farmer<ScottishFish>>> beans = getBeans(new TypeLiteral<Farmer<ScottishFish>>() {
        });
        assertEquals(beans.size(), 1);
        assertTrue(beans.iterator().next().getTypes().contains(ScottishFishFarmer.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "j"), @SpecAssertion(section = "2.2.1", id = "i") })
    public void testResolveByTypeWithArray() throws Exception {
        assertEquals(getBeans(Spider[].class).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "i"), @SpecAssertion(section = "5.2.5", id = "aa"),
            @SpecAssertion(section = "5.2.5", id = "ab"), @SpecAssertion(section = "5.2.7", id = "b"),
            @SpecAssertion(section = "5.2.7", id = "c") })
    public void testResolveByTypeWithPrimitives() {

        assertEquals(getBeans(Double.class, AnyLiteral.INSTANCE).size(), 2);
        assertEquals(getBeans(double.class, AnyLiteral.INSTANCE).size(), 2);

        Double min = getInstanceByType(Double.class, new AnnotationLiteral<Min>() {
        });
        double max = getInstanceByType(double.class, new AnnotationLiteral<Max>() {
        });

        assertEquals(min, NumberProducer.min);
        assertEquals(max, NumberProducer.max);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "ld"), @SpecAssertion(section = "5.2.6", id = "b") })
    public void testResolveByTypeWithNonBindingMembers() throws Exception {

        Set<Bean<Animal>> beans = getBeans(Animal.class, new ExpensiveLiteral() {
            public int cost() {
                return 60;
            }

            public boolean veryExpensive() {
                return true;
            }

        }, new AnnotationLiteral<Whitefish>() {
        });
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
    @SpecAssertion(section = "2.2.2", id = "a")
    public void testBeanTypesOnManagedBean() {
        assertEquals(getBeans(Canary.class).size(), 1);
        Bean<Canary> bean = getUniqueBean(Canary.class);
        assertTrue(getBeans(Bird.class).isEmpty());
        assertTrue(typeSetMatches(bean.getTypes(), Canary.class, Object.class));
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "e")
    public void testGenericBeanTypesOnManagedBean() {
        assertEquals(getBeans(AUSTRALIAN_FLIGHTLESS_BIRD).size(), 1);
        assertTrue(getBeans(Emu.class).isEmpty());
        assertTrue(getBeans(EUROPEAN_FLIGHTLESS_BIRD).isEmpty());
        Bean<FlightlessBird<Australian>> bean = getUniqueBean(AUSTRALIAN_FLIGHTLESS_BIRD);
        assertTrue(typeSetMatches(bean.getTypes(), AUSTRALIAN_FLIGHTLESS_BIRD.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "c")
    public void testBeanTypesOnProducerMethod() {
        assertEquals(getBeans(Parrot.class).size(), 1);
        assertTrue(getBeans(Bird.class).isEmpty());
        Bean<Parrot> bean = getUniqueBean(Parrot.class);
        assertTrue(typeSetMatches(bean.getTypes(), Parrot.class, Object.class));
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "h")
    public void testGenericBeanTypesOnProducerField() {
        assertEquals(getBeans(EUROPEAN_CAT, TAME).size(), 1);
        assertTrue(getBeans(DomesticCat.class, TAME).isEmpty());
        Bean<Cat<European>> bean = getUniqueBean(EUROPEAN_CAT, TAME);
        assertTrue(typeSetMatches(bean.getTypes(), EUROPEAN_CAT.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "g")
    public void testGenericBeanTypesOnProducerMethod() {
        assertEquals(getBeans(AFRICAN_CAT, WILD).size(), 1);
        assertTrue(getBeans(Lion.class, WILD).isEmpty());
        Bean<Cat<African>> bean = getUniqueBean(AFRICAN_CAT, WILD);
        assertTrue(typeSetMatches(bean.getTypes(), AFRICAN_CAT.getType(), Object.class));
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "d")
    public void testBeanTypesOnProducerField() {
        assertEquals(getBeans(Dove.class).size(), 1);
        assertTrue(getBeans(Bird.class).isEmpty());
        Bean<Dove> bean = getUniqueBean(Dove.class);
        assertTrue(typeSetMatches(bean.getTypes(), Dove.class, Object.class));
    }
}

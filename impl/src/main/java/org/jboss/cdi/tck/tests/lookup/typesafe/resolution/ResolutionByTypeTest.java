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

import static org.jboss.cdi.tck.TestGroups.INJECTION;
import static org.jboss.cdi.tck.TestGroups.POLICY;
import static org.jboss.cdi.tck.TestGroups.PRODUCER_METHOD;
import static org.jboss.cdi.tck.TestGroups.RESOLUTION;
import static org.jboss.cdi.tck.TestGroups.REWRITE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

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

    @Test(groups = RESOLUTION)
    @SpecAssertion(section = "5.2", id = "lb")
    public void testDefaultBindingTypeAssumed() throws Exception {
        Set<Bean<Tuna>> possibleTargets = getBeans(Tuna.class);
        assert possibleTargets.size() == 1;
        assert possibleTargets.iterator().next().getTypes().contains(Tuna.class);
    }

    @Test(groups = RESOLUTION)
    @SpecAssertion(section = "5.2", id = "hc")
    public void testResolveByType() throws Exception {

        assert getBeans(Tuna.class, new DefaultLiteral()).size() == 1;

        assert getBeans(Tuna.class).size() == 1;

        Set<Bean<Animal>> beans = getBeans(Animal.class, new AnnotationLiteral<FishILike>() {
        });
        assert beans.size() == 3;
        List<Class<? extends Animal>> classes = new ArrayList<Class<? extends Animal>>();
        for (Bean<Animal> bean : beans) {
            if (bean.getTypes().contains(Salmon.class)) {
                classes.add(Salmon.class);
            } else if (bean.getTypes().contains(SeaBass.class)) {
                classes.add(SeaBass.class);
            } else if (bean.getTypes().contains(Haddock.class)) {
                classes.add(Haddock.class);
            }
        }
        assert classes.contains(Salmon.class);
        assert classes.contains(SeaBass.class);
        assert classes.contains(Haddock.class);
    }

    @Test(groups = INJECTION)
    @SpecAssertions({ @SpecAssertion(section = "2.3.4", id = "b"), @SpecAssertion(section = "5.2", id = "lc"),
            @SpecAssertion(section = "2.3.3", id = "d"), @SpecAssertion(section = "5.2", id = "la"),
            @SpecAssertion(section = "5.2.6", id = "a"), @SpecAssertion(section = "5.2.6", id = "d") })
    public void testAllQualifiersSpecifiedForResolutionMustAppearOnBean() {
        assert getBeans(Animal.class, new ChunkyLiteral(), new AnnotationLiteral<Whitefish>() {
        }).size() == 1;
        assert getBeans(Animal.class, new ChunkyLiteral(), new AnnotationLiteral<Whitefish>() {
        }).iterator().next().getTypes().contains(Cod.class);

        assert getBeans(ScottishFish.class, new AnnotationLiteral<Whitefish>() {
        }).size() == 2;
        List<Class<? extends Animal>> classes = new ArrayList<Class<? extends Animal>>();
        for (Bean<ScottishFish> bean : getBeans(ScottishFish.class, new AnnotationLiteral<Whitefish>() {
        })) {
            if (bean.getTypes().contains(Cod.class)) {
                classes.add(Cod.class);
            } else if (bean.getTypes().contains(Sole.class)) {
                classes.add(Sole.class);
            }
        }
        assert classes.contains(Cod.class);
        assert classes.contains(Sole.class);

    }

    @Test(groups = { RESOLUTION })
    @SpecAssertions({ @SpecAssertion(section = "5.2", id = "ka") })
    public void testResolveByTypeWithTypeParameter() throws Exception {
        assert getBeans(new TypeLiteral<Farmer<ScottishFish>>() {
        }).size() == 1;
        assert getBeans(new TypeLiteral<Farmer<ScottishFish>>() {
        }).iterator().next().getTypes().contains(ScottishFishFarmer.class);
    }

    @Test(groups = { RESOLUTION, PRODUCER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "5.2", id = "j"), @SpecAssertion(section = "2.2.1", id = "i") })
    public void testResolveByTypeWithArray() throws Exception {
        assert getBeans(Spider[].class).size() == 1;
    }

    @Test(groups = { RESOLUTION })
    @SpecAssertions({ @SpecAssertion(section = "5.2", id = "i"), @SpecAssertion(section = "5.2.4", id = "aa"),
            @SpecAssertion(section = "5.2.4", id = "ab"), @SpecAssertion(section = "5.2.6", id = "b"),
            @SpecAssertion(section = "5.2.6", id = "c") })
    public void testResolveByTypeWithPrimitives() {
        assert getBeans(Double.class, AnyLiteral.INSTANCE).size() == 2;
        assert getBeans(double.class, AnyLiteral.INSTANCE).size() == 2;

        Double min = getInstanceByType(Double.class, new AnnotationLiteral<Min>() {
        });
        double max = getInstanceByType(double.class, new AnnotationLiteral<Max>() {
        });

        assert min.equals(NumberProducer.min);
        assert NumberProducer.max.equals(max);
    }

    @Test(groups = RESOLUTION)
    @SpecAssertions({ @SpecAssertion(section = "5.2", id = "ld"), @SpecAssertion(section = "5.2.5", id = "b") })
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
        assert beans.size() == 2;

        Set<Type> classes = new HashSet<Type>();
        for (Bean<Animal> bean : beans) {
            classes.addAll(bean.getTypes());
        }
        assert classes.contains(Halibut.class);
        assert classes.contains(RoundWhitefish.class);
        assert !classes.contains(Sole.class);
    }

    @Test(groups = { POLICY, REWRITE })
    // TODO Needs to be rewritten to use injection PLM
    @SpecAssertion(section = "5.1.4", id = "i")
    public void testPolicyNotAvailableInNonDeploymentArchive() throws Exception {
        Set<Bean<Spider>> spiders = getBeans(Spider.class);
        Set<Type> types = new HashSet<Type>();
        for (Bean<Spider> spider : spiders) {
            types.addAll(spider.getTypes());
        }
        assert !types.contains(CrabSpider.class);
        assert !types.contains(DaddyLongLegs.class);

        assert getCurrentManager().getBeans("crabSpider").size() == 0;
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "a")
    public void testBeanTypesOnManagedBean() {
        assert getBeans(Canary.class).size() == 1;
        Bean<Canary> bean = getUniqueBean(Canary.class);
        assert getBeans(Bird.class).isEmpty();
        assert typeSetMatches(bean.getTypes(), Canary.class, Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "e")
    public void testGenericBeanTypesOnManagedBean() {
        assert getBeans(AUSTRALIAN_FLIGHTLESS_BIRD).size() == 1;
        assert getBeans(Emu.class).isEmpty();
        assert getBeans(EUROPEAN_FLIGHTLESS_BIRD).isEmpty();
        Bean<FlightlessBird<Australian>> bean = getUniqueBean(AUSTRALIAN_FLIGHTLESS_BIRD);
        assert typeSetMatches(bean.getTypes(), AUSTRALIAN_FLIGHTLESS_BIRD.getType(), Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "c")
    public void testBeanTypesOnProducerMethod() {
        assert getBeans(Parrot.class).size() == 1;
        assert getBeans(Bird.class).isEmpty();
        Bean<Parrot> bean = getUniqueBean(Parrot.class);
        assert typeSetMatches(bean.getTypes(), Parrot.class, Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "h")
    public void testGenericBeanTypesOnProducerField() {
        assert getBeans(EUROPEAN_CAT, TAME).size() == 1;
        assert getBeans(DomesticCat.class, TAME).isEmpty();
        Bean<Cat<European>> bean = getUniqueBean(EUROPEAN_CAT, TAME);
        assert typeSetMatches(bean.getTypes(), EUROPEAN_CAT.getType(), Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "g")
    public void testGenericBeanTypesOnProducerMethod() {
        assert getBeans(AFRICAN_CAT, WILD).size() == 1;
        assert getBeans(Lion.class, WILD).isEmpty();
        Bean<Cat<African>> bean = getUniqueBean(AFRICAN_CAT, WILD);
        assert typeSetMatches(bean.getTypes(), AFRICAN_CAT.getType(), Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.2", id = "d")
    public void testBeanTypesOnProducerField() {
        assert getBeans(Dove.class).size() == 1;
        assert getBeans(Bird.class).isEmpty();
        Bean<Dove> bean = getUniqueBean(Dove.class);
        assert typeSetMatches(bean.getTypes(), Dove.class, Object.class);
    }
}

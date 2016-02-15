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
package org.jboss.cdi.tck.tests.extensions.producer;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INJECTIONTARGET;
import static org.jboss.cdi.tck.cdi.Sections.PIT;
import static org.jboss.cdi.tck.cdi.Sections.PP;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.Producer;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Producer extension tests.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SuppressWarnings("serial")
@Test
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ProducerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerTest.class).withExtension(ProducerProcessor.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "ba"), @SpecAssertion(section = INJECTIONTARGET, id = "bb"),
            @SpecAssertion(section = INJECTIONTARGET, id = "bc") })
    public void testProduceAndInjectCallsInitializerAndConstructor() {
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
        Bean<Cat> catBean = getUniqueBean(Cat.class);
        Cat.reset();
        CreationalContext<Cat> ctx = getCurrentManager().createCreationalContext(catBean);
        Cat instance = injectionTarget.produce(ctx);
        assert Cat.isConstructorCalled();
        injectionTarget.inject(instance, ctx);
        assert Cat.isInitializerCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "c") })
    public void testDisposeDoesNothing() {
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();

        Cat cat = getContextualReference(Cat.class);
        injectionTarget.dispose(cat);
        // The instance should still be available
        cat.ping();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "da") })
    public void testGetInjectionPointsForFields() {
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
        assert injectionTarget.getInjectionPoints().size() == 3;
        boolean injectionPointPresent = false;
        for (InjectionPoint injectionPoint : injectionTarget.getInjectionPoints()) {
            if (injectionPoint.getType().equals(CatFoodDish.class)) {
                injectionPointPresent = true;
            }
        }
        assert injectionPointPresent;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "db"), @SpecAssertion(section = INJECTIONTARGET, id = "dc") })
    public void testGetInjectionPointsForConstructorAndInitializer() {
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
        assert injectionTarget.getInjectionPoints().size() == 3;
        boolean constructorIPPresent = false;
        boolean initializerMethodIPPresent = false;
        for (InjectionPoint injectionPoint : injectionTarget.getInjectionPoints()) {
            if (injectionPoint.getType().equals(LitterBox.class)) {
                constructorIPPresent = true;
            }
            if (injectionPoint.getType().equals(Bird.class)) {
                initializerMethodIPPresent = true;
            }
        }
        assert initializerMethodIPPresent;
        assert constructorIPPresent;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "eaa"), @SpecAssertion(section = PP, id = "aa"),
            @SpecAssertion(section = PP, id = "ba"), @SpecAssertion(section = PP, id = "ca"),
            @SpecAssertion(section = PP, id = "da"), @SpecAssertion(section = BEAN_DISCOVERY, id = "ha") })
    public void testProduceCallsProducerMethod() {
        Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
        Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        DogProducer.reset();
        Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
        assert DogProducer.isNoisyDogProducerCalled();
        assert dog.getColor().equals(DogProducer.NOISY_DOG_COLOR);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PP, id = "e"), @SpecAssertion(section = PP, id = "ga") })
    public void testSetProducerOverridesProducer() {
        ProducerProcessor.reset();
        assert getContextualReference(Cow.class, new AnnotationLiteral<Noisy>() {
        }) instanceof Cow;
        assert ProducerProcessor.isOverriddenCowProducerCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "eba"), @SpecAssertion(section = PP, id = "ab"),
            @SpecAssertion(section = PP, id = "bb"), @SpecAssertion(section = PP, id = "cb"),
            @SpecAssertion(section = PP, id = "db"), @SpecAssertion(section = BEAN_DISCOVERY, id = "hb") })
    public void testProduceAccessesProducerField() {
        Producer<Dog> producer = ProducerProcessor.getQuietDogProducer();
        Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Quiet>() {
        });
        DogProducer.reset();
        Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
        assert dog.getColor().equals(DogProducer.QUIET_DOG_COLOR);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "faa") })
    public void testProducerForMethodDisposesProduct() {

        Bean<Dog> dogBean = getUniqueBean(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
        DogProducer.reset();
        Dog dog = producer.produce(getCurrentManager().createCreationalContext(dogBean));
        assert DogProducer.isNoisyDogProducerCalled();
        producer.dispose(dog);
        assert DogProducer.isNoisyDogDisposerCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "g") })
    public void testInjectionPointsForProducerMethod() {
        Producer<Dog> producer = ProducerProcessor.getNoisyDogProducer();
        DogProducer.reset();
        assert producer.getInjectionPoints().size() == 1;
        assert producer.getInjectionPoints().iterator().next().getType().equals(DogBed.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "i"), @SpecAssertion(section = BEAN_DISCOVERY, id = "ba"),
            @SpecAssertion(section = PIT, id = "aaa") })
    public void testInjectionTargetInject() {
        InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
        Bean<Dog> dogBean = (Bean<Dog>) getCurrentManager().getBeans(Dog.class).iterator().next();
        CreationalContext<Dog> dogCreationalContext = getCurrentManager().createCreationalContext(dogBean);
        Dog dog = dogBean.create(dogCreationalContext);
        dog.setDogBone(null);
        injectionTarget.inject(dog, dogCreationalContext);
        assert dog.getDogBone() != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "j") })
    public void testInjectionTargetPostConstruct() {
        InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
        Dog dog = getContextualReference(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        Dog.setPostConstructCalled(false);
        injectionTarget.postConstruct(dog);
        assert Dog.isPostConstructCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "k") })
    public void testInjectionTargetPreDestroy() {
        InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
        Dog dog = getContextualReference(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        Dog.setPreDestroyCalled(false);
        injectionTarget.preDestroy(dog);
        assert Dog.isPreDestroyCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "bb"), @SpecAssertion(section = PIT, id = "ea") })
    public void testSettingInjectionTargetReplacesIt() {
        CheckableInjectionTarget.setInjectCalled(false);
        getContextualReference(BirdCage.class);
        assert CheckableInjectionTarget.isInjectCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aba") })
    public void testGetAnnotatedTypeOnProcessInjectionTarget() {
        assert ProducerProcessor.getDogAnnotatedType() != null;
        assert ProducerProcessor.getDogAnnotatedType().getBaseType().equals(Dog.class);
    }

}

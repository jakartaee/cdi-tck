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

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.INJECTIONTARGET;
import static org.jboss.cdi.tck.cdi.Sections.INJECTIONTARGET_EE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_TARGET;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_PRODUCER;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;

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
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Producer extension tests.
 *
 * @author David Allen
 * @author Martin Kouba
 * @author Kirill Gaevskii
 */
@SuppressWarnings("serial")
@Test
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ProducerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerTest.class)
                .withExtension(ProducerProcessor.class).build();
    }

    @Test
    @SpecAssertions({ 
        @SpecAssertion(section = INJECTIONTARGET, id = "ba"),
        @SpecAssertion(section = INJECTIONTARGET, id = "bb"),
        @SpecAssertion(section = INJECTIONTARGET, id = "bc")
    })
    public void testProduceAndInjectCallsInitializerAndConstructor() {
        Cat.reset();
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
        CreationalContext<Cat> ctx = getCurrentManager().createCreationalContext(null);
        Cat instance = injectionTarget.produce(ctx);
        assert Cat.isConstructorCalled();
        injectionTarget.inject(instance, ctx);
        assert Cat.isInitializerCalled();
    }
    
    @Test
    @SpecAssertions({
        @SpecAssertion(section = INJECTIONTARGET, id = "bd"),
        @SpecAssertion(section = INJECTIONTARGET, id = "be")
    })
    public void testInterceptorAndDecoratorStackBuilt() {
        InjectionTarget<Cat> injectionTarget = ProducerProcessor.getCatInjectionTarget();
        CreationalContext<Cat> ctx = getCurrentManager().createCreationalContext(null);
        Cat instance = injectionTarget.produce(ctx);
        Assert.assertEquals(instance.foo(), 11);
        Assert.assertEquals(instance.saySomething(), "Meow meow");
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
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "eaa"), @SpecAssertion(section = PROCESS_PRODUCER, id = "aa"),
            @SpecAssertion(section = PROCESS_PRODUCER, id = "ba"), @SpecAssertion(section = PROCESS_PRODUCER, id = "ca"),
            @SpecAssertion(section = PROCESS_PRODUCER, id = "da"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jb") })
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
    @SpecAssertions({ @SpecAssertion(section = PROCESS_PRODUCER, id = "e"), @SpecAssertion(section = PROCESS_PRODUCER, id = "ga") })
    public void testSetProducerOverridesProducer() {
        ProducerProcessor.reset();
        assert getContextualReference(Cow.class, new AnnotationLiteral<Noisy>() {
        }) instanceof Cow;
        assert ProducerProcessor.isOverriddenCowProducerCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "eba"), @SpecAssertion(section = PROCESS_PRODUCER, id = "ab"),
            @SpecAssertion(section = PROCESS_PRODUCER, id = "bb"), @SpecAssertion(section = PROCESS_PRODUCER, id = "cb"),
            @SpecAssertion(section = PROCESS_PRODUCER, id = "db"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jb") })
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
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "i"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "c"),
            @SpecAssertion(section = PROCESS_INJECTION_TARGET, id = "aaa"), @SpecAssertion(section = INJECTIONTARGET_EE, id = "a") })
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
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "j"), @SpecAssertion(section = INJECTIONTARGET_EE, id = "b") })
    public void testInjectionTargetPostConstruct() {
        InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
        Dog dog = getContextualReference(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        Dog.setPostConstructCalled(false);
        injectionTarget.postConstruct(dog);
        assert Dog.isPostConstructCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTIONTARGET, id = "k"),  @SpecAssertion(section = INJECTIONTARGET_EE, id = "c") })
    public void testInjectionTargetPreDestroy() {
        InjectionTarget<Dog> injectionTarget = ProducerProcessor.getDogInjectionTarget();
        Dog dog = getContextualReference(Dog.class, new AnnotationLiteral<Noisy>() {
        });
        Dog.setPreDestroyCalled(false);
        injectionTarget.preDestroy(dog);
        assert Dog.isPreDestroyCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET, id = "bb"), @SpecAssertion(section = PROCESS_INJECTION_TARGET, id = "ea") })
    public void testSettingInjectionTargetReplacesIt() {
        CheckableInjectionTarget.setInjectCalled(false);
        getContextualReference(BirdCage.class);
        assert CheckableInjectionTarget.isInjectCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET, id = "aba") })
    public void testGetAnnotatedTypeOnProcessInjectionTarget() {
        assert ProducerProcessor.getDogAnnotatedType() != null;
        assert ProducerProcessor.getDogAnnotatedType().getBaseType().equals(Dog.class);
    }
}

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
package org.jboss.cdi.tck.tests.event.bindingTypes;

import static org.jboss.cdi.tck.cdi.Sections.EVENT_TYPES_AND_QUALIFIER_TYPES;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class EventBindingTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventBindingTypesTest.class).build();
    }

    @Test
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "d")
    public void testEventBindingTypeTargetsMethodFieldParameterElementTypes() {
        Animal animal = new Animal();
        getCurrentManager().fireEvent(animal, new TameAnnotationLiteral());
        getContextualReference(AnimalAssessment.class).classifyAsTame(animal);
    }

    @Test
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "d")
    public void testEventBindingTypeTargetsFieldParameterElementTypes() {
        Animal animal = new Animal();
        getCurrentManager().fireEvent(animal, new WildAnnotationLiteral());
        getContextualReference(AnimalAssessment.class).classifyAsWild(animal);
    }

    /**
     * This test ensures that an event binding type without runtime retention is effectively invisible
     */
    @Test
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "d")
    public void testNonRuntimeBindingTypeIsNotAnEventBindingType() {
        DiscerningObserver observer = getContextualReference(DiscerningObserver.class);
        observer.reset();
        EventEmitter emitter = getContextualReference(EventEmitter.class);
        emitter.fireEvent();
        assert observer.getNumTimesAnyBindingTypeEventObserved() == 1;
        assert observer.getNumTimesNonRuntimeBindingTypeObserved() == 1;
        emitter.fireEventWithNonRuntimeBindingType();
        assert observer.getNumTimesAnyBindingTypeEventObserved() == 2;
        assert observer.getNumTimesNonRuntimeBindingTypeObserved() == 2;
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "d")
    public void testFireEventWithNonRuntimeBindingTypeFails() {
        getCurrentManager().fireEvent(new Animal(), new AnnotationLiteral<NonRuntimeBindingType>() {
        });
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "g")
    public void testFireEventWithNonBindingAnnotationsFails() {
        getCurrentManager().fireEvent(new Animal(), new AnnotationLiteral<NonBindingType>() {
        });
    }

    @Test
    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "i")
    public void testEventAlwaysHasAnyBinding() {
        Bean<Event<Animal>> animalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {
        }, new WildAnnotationLiteral());
        assert animalEventBean.getQualifiers().contains(AnyLiteral.INSTANCE);

        Bean<Event<Animal>> tameAnimalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {
        }, new TameAnnotationLiteral());
        assert tameAnimalEventBean.getQualifiers().contains(AnyLiteral.INSTANCE);

        Bean<Event<Animal>> wildAnimalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {
        }, new WildAnnotationLiteral());
        assert wildAnimalEventBean.getQualifiers().contains(AnyLiteral.INSTANCE);
    }
}

/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_CONSTRUCTOR_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_FIELD_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_METHOD_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_PARAMETER_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_TYPE_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.DisposesLiteral;
import org.jboss.cdi.tck.literals.ProducesLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class AnnotatedTypeConfiguratorTest extends AbstractTest {

    @Inject
    Instance<Countryside> countrysideInstance;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AnnotatedTypeConfiguratorTest.class)
                .withClasses(ProducesLiteral.class, DisposesLiteral.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtensions(ProcessAnnotatedTypeObserver.class
                ).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bd"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bg"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bi"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bj"),
            @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "b"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "b"),
            @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "f"),
            @SpecAssertion(section = ANNOTATED_FIELD_CONFIGURATOR, id = "b"),
            @SpecAssertion(section = ANNOTATED_PARAMETER_CONFIGURATOR, id = "b")
    })
    public void addMethodsOfAnnotationTypecConfigurator() {
        Bean<Dog> dogBean = getUniqueBean(Dog.class);
        CreationalContext<Dog> creationalContext = getCurrentManager().createCreationalContext(dogBean);
        Dog dog = dogBean.create(creationalContext);

        assertNotNull(dogBean);
        assertEquals(dogBean.getScope(), RequestScoped.class);

        assertNotNull(dog.getFeed());
        assertEquals(dog.getName(), DogDependenciesProducer.dogName);

        List<InjectionPoint> dogsInjectionPoints = dogBean.getInjectionPoints().stream()
                .filter(injectionPoint -> injectionPoint.getQualifiers().contains(new Dogs.DogsLiteral())).collect(
                        Collectors.toList());
        assertEquals(dogsInjectionPoints.size(), 2);
        Optional<InjectionPoint> feedIpOptional = dogsInjectionPoints.stream()
                .filter(injectionPoint -> injectionPoint.getType().equals(Feed.class))
                .findFirst();
        assertTrue(feedIpOptional.isPresent());

        dogBean.destroy(dog, creationalContext);
        assertTrue(DogDependenciesProducer.disposerCalled.get());

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "be"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bh"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bj"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bl"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "c"),
            @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "c"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "f"),
            @SpecAssertion(section = ANNOTATED_FIELD_CONFIGURATOR, id = "c"),
            @SpecAssertion(section = ANNOTATED_PARAMETER_CONFIGURATOR, id = "c") })
    public void removeMethodsOfAnnotationTypeConfigurator() {
        Bean<Cat> catBean = getUniqueBean(Cat.class);
        CreationalContext<Cat> creationalContext = getCurrentManager().createCreationalContext(catBean);
        Cat cat = catBean.create(creationalContext);

        assertNotNull(catBean);
        assertEquals(catBean.getScope(), Dependent.class);

        assertNull(cat.getFeed());
        Set<Bean<Feed>> catFeedBeans = getBeans(Feed.class, Cats.CatsLiteral.INSTANCE);
        assertEquals(catFeedBeans.size(), 0);

        getCurrentManager().getEvent().select(Feed.class).fire(new Feed());
        assertFalse(cat.isFeedObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = ANNOTATED_FIELD_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = ANNOTATED_PARAMETER_CONFIGURATOR, id = "a") })
    public void annotatedTypesAndMemebersEqual() {
        assertTrue(ProcessAnnotatedTypeObserver.annotatedTypesEqual.get());
        assertTrue(ProcessAnnotatedTypeObserver.annotatedMethodEqual.get());
        assertTrue(ProcessAnnotatedTypeObserver.annotatedFieldEqual.get());
        assertTrue(ProcessAnnotatedTypeObserver.annotatedConstructorEqual.get());
        assertTrue(ProcessAnnotatedTypeObserver.annotatedParameterEqual.get());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bf"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "bk"),
            @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "d"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "d"),
            @SpecAssertion(section = ANNOTATED_PARAMETER_CONFIGURATOR, id = "d"),
            @SpecAssertion(section = ANNOTATED_METHOD_CONFIGURATOR, id = "e"),
            @SpecAssertion(section = ANNOTATED_FIELD_CONFIGURATOR, id = "d") })
    public void annotationsRemovedFromAnimalShelter() {
        Bean<AnimalShelter> animalShelterBean = getUniqueBean(AnimalShelter.class);

        CreationalContext<AnimalShelter> creationalContext = getCurrentManager().createCreationalContext(animalShelterBean);
        AnimalShelter animalShelter = animalShelterBean.create(creationalContext);
        getCurrentManager().getEvent().select(Room.class, Cats.CatsLiteral.INSTANCE, Any.Literal.INSTANCE).fire(new Room());

        assertNotNull(animalShelterBean);
        assertEquals(animalShelterBean.getName(), null);
        assertEquals(animalShelterBean.getScope(), Dependent.class);
        assertFalse(animalShelter.isPostConstructCalled());
        assertFalse(animalShelter.isRoomObserved());
        assertNull(animalShelter.getCat());
    }

    @Test
    @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bba")
    public void configuratorInitializedWithOriginalAT() {
        AnnotatedType<Cat> catAT = getCurrentManager().getExtension(ProcessAnnotatedTypeObserver.class).getOriginalCatAT();
        assertTrue(catAT.isAnnotationPresent(RequestScoped.class));
        AnnotatedConstructor<Cat> annotatedConstructor = catAT.getConstructors().stream()
                .filter(ac -> ac.getParameters().size() == 1 && ac.getParameters().get(0).getBaseType().equals(Feed.class))
                .findFirst().get();
        assertTrue(annotatedConstructor.getParameters().iterator().next().isAnnotationPresent(Cats.class));
        assertTrue(annotatedConstructor.isAnnotationPresent(Inject.class));
    }

    @Test
    @SpecAssertion(section = ANNOTATED_CONSTRUCTOR_CONFIGURATOR, id = "e")
    public void configureAndTestConstructorAnnotatedParams() {
        Assert.assertFalse(countrysideInstance.isUnsatisfied());
        Countryside countryside = countrysideInstance.get();
        assertEquals(countryside.getWildDog().getName(), "wild dog");
        assertEquals(countryside.getWildCat().getName(), "wild cat");

    }

}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.annotatedTypeConfigurator;

import static org.jboss.cdi.tck.cdi.Sections.ANNOTATED_TYPE_CONFIGURATOR;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.DisposesLiteral;
import org.jboss.cdi.tck.literals.ProducesLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class AnnotatedTypeConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AnnotatedTypeConfiguratorTest.class)
                .withClasses(ProducesLiteral.class, DisposesLiteral.class)
                .withExtension(ProcessAnnotatedTypeObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "a"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "b"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "c"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "e"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "g"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "i"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "k"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "m"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "o") })
    public void addMethodsOfAnnotationTypecConfigurator() {
        Bean<Dog> dogBean = getUniqueBean(Dog.class);
        CreationalContext<Dog> creationalContext = getCurrentManager().createCreationalContext(dogBean);
        Dog dog = dogBean.create(creationalContext);

        Assert.assertNotNull(dogBean);
        Assert.assertEquals(dogBean.getScope(), RequestScoped.class);

        Assert.assertNotNull(dog.getFeed());
        Assert.assertEquals(dog.getName(), DogDependenciesProducer.dogName);

        List<InjectionPoint> dogsInjectionPoints = dogBean.getInjectionPoints().stream()
                .filter(injectionPoint -> injectionPoint.getQualifiers().contains(new Dogs.DogsLiteral())).collect(
                        Collectors.toList());
        Assert.assertEquals(dogsInjectionPoints.size(), 2);
        Optional<InjectionPoint> feedIpOptional = dogsInjectionPoints.stream().filter(injectionPoint -> injectionPoint.getType().equals(Feed.class))
                .findFirst();
        Assert.assertTrue(feedIpOptional.isPresent());

        dogBean.destroy(dog, creationalContext);
        Assert.assertTrue(DogDependenciesProducer.disposerCalled.get());

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "d"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "f"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "h"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "j"),
            @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "l"), @SpecAssertion(section = ANNOTATED_TYPE_CONFIGURATOR, id = "n") })
    public void removeMethodsOfAnnotationTypeConfigurator() {
        Bean<Cat> catBean = getUniqueBean(Cat.class);
        CreationalContext<Cat> creationalContext = getCurrentManager().createCreationalContext(catBean);
        Cat cat = catBean.create(creationalContext);

        Assert.assertNotNull(catBean);
        Assert.assertEquals(catBean.getScope(), Dependent.class);

        Assert.assertNull(cat.getFeed());
        Set<Bean<Feed>> catFeedBeans = getBeans(Feed.class, Cats.CatsLiteral.INSTANCE);
        Assert.assertEquals(catFeedBeans.size(), 0);

        getCurrentManager().fireEvent(new Feed());
        Assert.assertFalse(cat.isFeedObserved());
    }

}

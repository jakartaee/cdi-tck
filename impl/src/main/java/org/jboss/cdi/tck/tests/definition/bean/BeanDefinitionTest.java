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
package org.jboss.cdi.tck.tests.definition.bean;

import static org.jboss.cdi.tck.TestGroups.PRODUCER_METHOD;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
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
 * This test class should be used for common assertions about managed beans (not session beans)
 * 
 * @author Pete Muir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class BeanDefinitionTest extends AbstractTest {

    private static Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanDefinitionTest.class).build();
    }

    // TODO This should actually somehow test the reverse as well - that the container
    // throws a definition exception if any of these occur

    @Test
    @SpecAssertion(section = "2", id = "a")
    public void testBeanTypesNonEmpty() {
        assert getBeans(RedSnapper.class).size() == 1;
        assert getBeans(RedSnapper.class).iterator().next().getTypes().size() > 0;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2", id = "b"), @SpecAssertion(section = "11.1", id = "ba") })
    public void testQualifiersNonEmpty() {
        assert getBeans(RedSnapper.class).size() == 1;
        assert getBeans(RedSnapper.class).iterator().next().getQualifiers().size() > 0;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2", id = "c"), @SpecAssertion(section = "2.4", id = "a"),
            @SpecAssertion(section = "3.1.3", id = "ba"), @SpecAssertion(section = "11.1", id = "ba") })
    public void testHasScopeType() {
        assert getBeans(RedSnapper.class).size() == 1;
        assert getBeans(RedSnapper.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "2.2.1", id = "j"), @SpecAssertion(section = "5.2.5", id = "ba"),
            @SpecAssertion(section = "11.1", id = "bd") })
    public void testIsNullable() throws Exception {
        assert getBeans(int.class).size() == 1;
        Bean<Integer> bean = getBeans(int.class).iterator().next();
        assert !bean.isNullable();
        assert getBeans(Animal.class, TAME_LITERAL).size() == 1;
        Bean<Animal> animalBean = getBeans(Animal.class, TAME_LITERAL).iterator().next();
        assert animalBean.isNullable();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.1.2", id = "a"), @SpecAssertion(section = "2.2", id = "a"),
            @SpecAssertion(section = "2.2.1", id = "a"), @SpecAssertion(section = "2.2.1", id = "d"),
            @SpecAssertion(section = "2.2.1", id = "e"), @SpecAssertion(section = "2.2", id = "l"),
            @SpecAssertion(section = "11.1", id = "ba") })
    public void testBeanTypes() {
        assert getBeans(Tarantula.class).size() == 1;
        Bean<Tarantula> bean = getBeans(Tarantula.class).iterator().next();
        assert bean.getTypes().size() == 6;
        assert bean.getTypes().contains(Tarantula.class);
        assert bean.getTypes().contains(Spider.class);
        assert bean.getTypes().contains(Animal.class);
        assert bean.getTypes().contains(Object.class);
        assert bean.getTypes().contains(DeadlySpider.class);
        assert bean.getTypes().contains(DeadlyAnimal.class);
    }

    @Test
    @SpecAssertion(section = "2.2.3", id = "a")
    @SuppressWarnings("unused")
    public void testBeanClientCanCastBeanInstanceToAnyBeanType() {
        assert getBeans(Tarantula.class).size() == 1;
        Bean<Tarantula> bean = getBeans(Tarantula.class).iterator().next();
        Tarantula tarantula = getCurrentManager().getContext(bean.getScope()).get(bean);

        Spider spider = tarantula;
        Animal animal = tarantula;
        Object obj = tarantula;
        DeadlySpider deadlySpider = tarantula;
        DeadlyAnimal deadlyAnimal = tarantula;
    }

    @Test
    @SpecAssertion(section = "2.2.1", id = "c")
    public void testAbstractApiType() {
        assert getBeans(FriendlyAntelope.class).size() == 1;
        Bean<FriendlyAntelope> bean = getBeans(FriendlyAntelope.class).iterator().next();
        assert bean.getTypes().size() == 4;
        assert bean.getTypes().contains(FriendlyAntelope.class);
        assert bean.getTypes().contains(AbstractAntelope.class);
        assert bean.getTypes().contains(Animal.class);
        assert bean.getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertion(section = "2.2.1", id = "d")
    public void testFinalApiType() {
        assert !getBeans(DependentFinalTuna.class).isEmpty();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.1.3", id = "bd"), @SpecAssertion(section = "11.1", id = "ba") })
    public void testMultipleStereotypes() {
        Bean<ComplicatedTuna> tunaBean = getBeans(ComplicatedTuna.class).iterator().next();
        assert tunaBean.getScope().equals(RequestScoped.class);
        assert tunaBean.getName().equals("complicatedTuna");
    }

    @Test
    @SpecAssertion(section = "3.1.3", id = "c")
    public void testBeanExtendsAnotherBean() {
        assert !getBeans(Spider.class).isEmpty();
        assert !getBeans(Tarantula.class).isEmpty();
    }

    @Test
    @SpecAssertion(section = "11.1", id = "bb")
    public void testBeanClassOnSimpleBean() {
        Set<Bean<Horse>> beans = getBeans(Horse.class);
        assert beans.size() == 1;
        assert beans.iterator().next().getBeanClass().equals(Horse.class);
    }
}
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

import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.CONCEPTS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_MANAGED_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.MANAGED_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.SCOPES;
import static org.jboss.cdi.tck.cdi.Sections.TYPECASTING_BETWEEN_BEAN_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;

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
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanDefinitionTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONCEPTS, id = "a")
    public void testBeanTypesNonEmpty() {
        assertEquals(getBeans(RedSnapper.class).size(), 1);
        assertFalse(getBeans(RedSnapper.class).iterator().next().getTypes().isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONCEPTS, id = "b"), @SpecAssertion(section = BEAN, id = "ba") })
    public void testQualifiersNonEmpty() {
        assertEquals(getBeans(RedSnapper.class).size(), 1);
        assertFalse(getBeans(RedSnapper.class).iterator().next().getQualifiers().isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONCEPTS, id = "c"), @SpecAssertion(section = SCOPES, id = "a"),
            @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "ba"), @SpecAssertion(section = BEAN, id = "ba") })
    public void testHasScopeType() {
        assertEquals(getBeans(RedSnapper.class).size(), 1);
        assertEquals(getBeans(RedSnapper.class).iterator().next().getScope(), RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_TYPES, id = "a"), @SpecAssertion(section = BEAN_TYPES, id = "a"),
            @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "a"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "d"),
            @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "e"), @SpecAssertion(section = BEAN_TYPES, id = "l"),
            @SpecAssertion(section = BEAN, id = "ba") })
    public void testBeanTypes() {
        assert getBeans(Tarantula.class).size() == 1;
        Bean<Tarantula> bean = getBeans(Tarantula.class).iterator().next();
        assertEquals(bean.getTypes().size(), 6);
        assertTrue(bean.getTypes().contains(Tarantula.class));
        assertTrue(bean.getTypes().contains(Spider.class));
        assertTrue(bean.getTypes().contains(Animal.class));
        assertTrue(bean.getTypes().contains(Object.class));
        assertTrue(bean.getTypes().contains(DeadlySpider.class));
        assertTrue(bean.getTypes().contains(DeadlyAnimal.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_TYPES, id = "a"), @SpecAssertion(section = BEAN_TYPES, id = "a"),
            @SpecAssertion(section = BEAN_TYPES, id = "l"), @SpecAssertion(section = BEAN, id = "ba") })
    public void testGenericBeanTypes() {
        assertEquals(getBeans(MyRawBean.class).size(), 1);
        Bean<MyGenericBean<?>> bean = (Bean<MyGenericBean<?>>) getCurrentManager().getBeans(MyGenericBean.class).iterator().next();
        assertEquals(bean.getTypes().size(), 5);

        assertTrue(containsClass(bean.getTypes(), MyGenericBean.class));
        assertFalse(bean.getTypes().contains(MyGenericBean.class));

        assertTrue(containsClass(bean.getTypes(), MyBean.class));
        assertFalse(bean.getTypes().contains(MyBean.class));

        assertTrue(bean.getTypes().contains(MyInterface.class));

        assertTrue(containsClass(bean.getTypes(), MySuperInterface.class));
        assertFalse(bean.getTypes().contains(MySuperInterface.class));

        assertTrue(bean.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_TYPES, id = "a"), @SpecAssertion(section = BEAN_TYPES, id = "a"),
            @SpecAssertion(section = BEAN_TYPES, id = "l"), @SpecAssertion(section = BEAN, id = "ba") })
    public void testRawBeanTypes() {
        assertEquals(getBeans(MyRawBean.class).size(), 1);
        Bean<MyRawBean> bean = getBeans(MyRawBean.class).iterator().next();
        assertEquals(bean.getTypes().size(), 5);
        assertTrue(bean.getTypes().contains(MyRawBean.class));
        assertTrue(bean.getTypes().contains(MyBean.class));
        assertTrue(bean.getTypes().contains(MyInterface.class));
        assertTrue(bean.getTypes().contains(MySuperInterface.class));
        assertTrue(bean.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertion(section = TYPECASTING_BETWEEN_BEAN_TYPES, id = "a")
    @SuppressWarnings("unused")
    public void testBeanClientCanCastBeanInstanceToAnyBeanType() {
        Set<Bean<Tarantula>> beans = getBeans(Tarantula.class);
        assertEquals(beans.size(), 1);
        Bean<Tarantula> bean = beans.iterator().next();
        Tarantula tarantula = getCurrentManager().getContext(bean.getScope()).get(bean);

        Animal animal = tarantula;
        Object obj = tarantula;
        DeadlySpider deadlySpider = tarantula;
        DeadlyAnimal deadlyAnimal = tarantula;
    }

    @Test
    @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "c")
    public void testAbstractApiType() {
        Set<Bean<FriendlyAntelope>> beans = getBeans(FriendlyAntelope.class);
        assertEquals(beans.size(), 1);
        Bean<FriendlyAntelope> bean = beans.iterator().next();
        assertEquals(bean.getTypes().size(), 4);
        assertTrue(bean.getTypes().contains(FriendlyAntelope.class));
        assertTrue(bean.getTypes().contains(AbstractAntelope.class));
        assertTrue(bean.getTypes().contains(Animal.class));
        assertTrue(bean.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "d")
    public void testFinalApiType() {
        assertFalse(getBeans(DependentFinalTuna.class).isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "bd"), @SpecAssertion(section = BEAN, id = "ba") })
    public void testMultipleStereotypes() {
        Bean<ComplicatedTuna> tunaBean = getBeans(ComplicatedTuna.class).iterator().next();
        assertEquals(tunaBean.getScope(), RequestScoped.class);
        assertEquals(tunaBean.getName(), "complicatedTuna");
    }

    @Test
    @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "c")
    public void testBeanExtendsAnotherBean() {
        assertFalse(getBeans(Spider.class).isEmpty());
        assertFalse(getBeans(Tarantula.class).isEmpty());
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bb")
    public void testBeanClassOnSimpleBean() {
        Set<Bean<Horse>> beans = getBeans(Horse.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), Horse.class);
    }

    private static boolean containsClass(Set<Type> types, Class<?> clazz) {
        for (Type type : types) {
            if (type instanceof Class<?>) {
                if (((Class<?>) type).equals(clazz)) {
                    return true;
                }
            }
            if (type instanceof ParameterizedType) {
                if (((ParameterizedType) type).getRawType().equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.qualifier.builtin;

import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_QUALIFIERS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class BuiltInQualifierDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BuiltInQualifierDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "aa"), @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab") })
    public void testDefaultQualifierDeclaredInJava() {
        Bean<Order> order = getBeans(Order.class).iterator().next();
        assertEquals(order.getQualifiers().size(), 2);
        assertTrue(order.getQualifiers().contains(new DefaultLiteral()));
        assertTrue(order.getQualifiers().contains(AnyLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "b"), @SpecAssertion(section = BEAN, id = "c") })
    public void testDefaultQualifierForInjectionPoint() {
        Bean<Order> order = getBeans(Order.class).iterator().next();
        assertEquals(order.getInjectionPoints().size(), 1);
        InjectionPoint injectionPoint = order.getInjectionPoints().iterator().next();
        assertTrue(injectionPoint.getQualifiers().contains(new DefaultLiteral()));
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testNamedAndAnyBeanHasDefaultQualifier() {
        Bean<NamedAnyBean> nameAnyBeanBean = getUniqueBean(NamedAnyBean.class, AnyLiteral.INSTANCE);
        assertEquals(nameAnyBeanBean.getQualifiers().size(), 3);
        checkSetContainsAllQuallifiers(nameAnyBeanBean.getQualifiers(), new DefaultLiteral(), new NamedLiteral("namedAnyBean"), AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testNamedBeanHasDefaultQualifier() {
        Bean<NamedBean> namedBean = getUniqueBean(NamedBean.class);
        assertEquals(namedBean.getQualifiers().size(), 3);
        checkSetContainsAllQuallifiers(namedBean.getQualifiers(), new DefaultLiteral(), new NamedLiteral("namedBean"), AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testAnyBeanHasDefaultQualifier() {
        Bean<AnyBean> anyBean = getUniqueBean(AnyBean.class, AnyLiteral.INSTANCE);
        assertEquals(anyBean.getQualifiers().size(), 2);
        checkSetContainsAllQuallifiers(anyBean.getQualifiers(), new DefaultLiteral(), AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testProducedNamedAndAnyBeanHasDefaultQualifier() {
        Bean<ProducedNamedAnyBean> producedNamedAnyBeanBean = getUniqueBean(ProducedNamedAnyBean.class);
        assertEquals(producedNamedAnyBeanBean.getQualifiers().size(), 3);
        checkSetContainsAllQuallifiers(producedNamedAnyBeanBean.getQualifiers(), new DefaultLiteral(), new NamedLiteral("producedNamedAnyBean"),
                AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testProducedNamedBeanHasDefaultQualifier() {
        Bean<ProducedNamedBean> producedNamedBeanBean = getUniqueBean(ProducedNamedBean.class);
        assertEquals(producedNamedBeanBean.getQualifiers().size(), 3);
        checkSetContainsAllQuallifiers(producedNamedBeanBean.getQualifiers(), new DefaultLiteral(), new NamedLiteral("producedNamedBean"), AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab")
    public void testProducedAnyBeanHasDefaultQualifier() {
        Bean<ProducedAnyBean> producedAnyBeanBean = getUniqueBean(ProducedAnyBean.class);
        assertEquals(producedAnyBeanBean.getQualifiers().size(), 2);
        checkSetContainsAllQuallifiers(producedAnyBeanBean.getQualifiers(), new DefaultLiteral(), AnyLiteral.INSTANCE);
    }

    private void checkSetContainsAllQuallifiers(Set<Annotation> qualifiersSet, Annotation... literals) {
        for (Annotation literal : literals) {
            assertTrue(qualifiersSet.contains(literal));
        }
    }

}

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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;

import javax.decorator.Decorator;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.NewLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class VerifyValuesTest extends AbstractTest {

    @Inject
    private VerifyingExtension extension;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(VerifyValuesTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives()
                                .clazz(Alpha.class.getName(), BravoProducer.class.getName(), CharlieProducer.class.getName())
                                .up().getOrCreateInterceptors().clazz(BravoInterceptor.class.getName()).up().getOrCreateDecorators()
                                .clazz(BravoDecorator.class.getName()).up()).withExtension(VerifyingExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "baa") })
    public void testManagedBeanAnnotated() {
        Annotated alphaAnnotated = extension.getAnnotatedMap().get(Alpha.class);
        assertNotNull(alphaAnnotated);
        assertTrue(alphaAnnotated instanceof AnnotatedType);
        @SuppressWarnings("unchecked")
        AnnotatedType<Alpha> alphaAnnotatedType = (AnnotatedType<Alpha>) alphaAnnotated;
        assertEquals(alphaAnnotatedType.getJavaClass(), Alpha.class);
        assertEquals(alphaAnnotatedType.getMethods().size(), 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ad") })
    public void testManagedBeanAttributes() {

        assertEquals(getCurrentManager().getBeans(Alpha.class, new NewLiteral(Alpha.class)).size(), 1);
        // No event is fired for any @New qualified bean
        assertEquals(extension.getAlphaAttributesObserved(), 1);

        BeanAttributes<Alpha> attributes = extension.getAlphaAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        verifyName(attributes, "alpha");
        assertTrue(attributes.isAlternative());
        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, Alpha.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), AlphaQualifier.class, Named.class, Any.class));

        // Event is not fired for managed bean that is not enabled
        assertNull(extension.getMike());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES_EE, id = "bab") })
    public void testSessionBeanAnnotated() {
        Annotated deltaAnnotated = extension.getAnnotatedMap().get(Delta.class);
        assertNotNull(deltaAnnotated);
        assertTrue(deltaAnnotated instanceof AnnotatedType);
        @SuppressWarnings("unchecked")
        AnnotatedType<Delta> deltaAnnotatedType = (AnnotatedType<Delta>) deltaAnnotated;
        assertEquals(deltaAnnotatedType.getJavaClass(), Delta.class);
        assertEquals(deltaAnnotatedType.getMethods().size(), 1);
        assertEquals(deltaAnnotatedType.getMethods().iterator().next().getJavaMember().getName(), "foo");
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES_EE, id = "bab"),
            @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testSessionBeanAttributes() {
        BeanAttributes<Delta> deltaAttributes = extension.getDeltaAttributes();
        assertNotNull(deltaAttributes);
        assertEquals(deltaAttributes.getScope(), Dependent.class);
        verifyName(deltaAttributes, "delta");
        assertFalse(deltaAttributes.isAlternative());

        assertTrue(typeSetMatches(deltaAttributes.getTypes(), Object.class, Delta.class));
        assertTrue(deltaAttributes.getStereotypes().isEmpty());
        assertTrue(annotationSetMatches(deltaAttributes.getQualifiers(), Named.class, Any.class, Default.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bac") })
    public void testProducerMethodAnnotated() {
        Annotated bravoAnnotated = extension.getAnnotatedMap().get(Bravo.class);
        assertNotNull(bravoAnnotated);
        assertTrue(bravoAnnotated instanceof AnnotatedMethod);
        @SuppressWarnings("unchecked")
        AnnotatedMethod<Bravo> bravoAnnotatedMethod = (AnnotatedMethod<Bravo>) bravoAnnotated;
        assertEquals(bravoAnnotatedMethod.getJavaMember().getName(), "createBravo");
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa") })
    public void testProducerMethodBeanAttributes() {
        BeanAttributes<Bravo> attributes = extension.getProducedBravoAttributes();
        assertNotNull(attributes);
        assertEquals(RequestScoped.class, attributes.getScope());
        verifyName(attributes, "createBravo");
        assertTrue(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), BravoInterface.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), BravoQualifier.class, Named.class, Any.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bad") })
    public void testProducerFieldAnnotated() {
        Annotated charlieAnnotated = extension.getAnnotatedMap().get(Charlie.class);
        assertNotNull(charlieAnnotated);
        assertTrue(charlieAnnotated instanceof AnnotatedField);
        @SuppressWarnings("unchecked")
        AnnotatedField<Charlie> charlieAnnotatedField = (AnnotatedField<Charlie>) charlieAnnotated;
        assertEquals(charlieAnnotatedField.getJavaMember().getName(), "charlie");
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa") })
    public void testProducerFieldBeanAttributes() {
        BeanAttributes<Charlie> attributes = extension.getProducedCharlieAttributes();
        assertNotNull(attributes);
        assertEquals(ApplicationScoped.class, attributes.getScope());
        verifyName(attributes, "charlie");
        assertFalse(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, Charlie.class, CharlieInterface.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), CharlieQualifier.class, Named.class, Any.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ab"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testInterceptorBeanAttributes() {
        BeanAttributes<BravoInterceptor> attributes = extension.getBravoInterceptorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, BravoInterceptor.class));
        assertTrue(attributes.getStereotypes().isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ac"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testDecoratorBeanAttributes() {
        BeanAttributes<BravoDecorator> attributes = extension.getBravoDecoratorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, BravoDecorator.class, BravoInterface.class));
        assertTrue(attributes.getStereotypes().size() == 1);
        assertTrue(attributes.getStereotypes().iterator().next().equals(Decorator.class));
    }

    private void verifyName(BeanAttributes<?> attributes, String name) {
        assertEquals(name, attributes.getName());
        for (Annotation qualifier : attributes.getQualifiers()) {
            if (Named.class.equals(qualifier.annotationType())) {
                assertEquals(name, ((Named) qualifier).value());
                return;
            }
        }
        fail("@Named qualifier not found.");
    }
}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.extensions.lifecycle.processBeanAttributes;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
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
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.NewLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class VerifyValuesTest extends AbstractJSR299Test {

    @Inject
    private VerifyingExtension extension;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(VerifyValuesTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createAlternatives()
                                .clazz(Alpha.class.getName(), BravoProducer.class.getName(), CharlieProducer.class.getName())
                                .up().createInterceptors().clazz(BravoInterceptor.class.getName()).up().createDecorators()
                                .clazz(BravoDecorator.class.getName()).up()).withExtension(VerifyingExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "baa") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa"), @SpecAssertion(section = "11.5.10", id = "ad") })
    public void testManagedBeanAttributes() {

        assertEquals(getCurrentManager().getBeans(Alpha.class, new NewLiteral(Alpha.class)).size(), 1);
        // No event is fired for any @New qualified bean
        assertEquals(extension.getAlphaAttributesObserved(), 1);

        BeanAttributes<Alpha> attributes = extension.getAlphaAttributes();
        assertNotNull(attributes);
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        verifyName(attributes, "alpha");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, Alpha.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), AlphaQualifier.class, Named.class, Any.class));

        // Event is not fired for managed bean that is not enabled
        assertNull(extension.getMike());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "bab") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa"), @SpecAssertion(section = "11.5.10", id = "bab"),
            @SpecAssertion(section = "11.5.10", id = "bb") })
    public void testSessionBeanAttributes() {
        BeanAttributes<Delta> deltaAttributes = extension.getDeltaAttributes();
        assertNotNull(deltaAttributes);
        assertEquals(deltaAttributes.getScope(), Dependent.class);
        verifyName(deltaAttributes, "delta");
        assertFalse(deltaAttributes.isAlternative());
        assertTrue(deltaAttributes.isNullable());

        assertTrue(typeSetMatches(deltaAttributes.getTypes(), Object.class, Delta.class));
        assertTrue(deltaAttributes.getStereotypes().isEmpty());
        assertTrue(annotationSetMatches(deltaAttributes.getQualifiers(), Named.class, Any.class, Default.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "bac") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa") })
    public void testProducerMethodBeanAttributes() {
        BeanAttributes<Bravo> attributes = extension.getProducedBravoAttributes();
        assertNotNull(attributes);
        assertEquals(RequestScoped.class, attributes.getScope());
        verifyName(attributes, "createBravo");
        assertTrue(attributes.isAlternative()); // because bravo producer is
        assertTrue(attributes.isNullable());

        assertTrue(typeSetMatches(attributes.getTypes(), BravoInterface.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), BravoQualifier.class, Named.class, Any.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "bad") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "aa") })
    public void testProducerFieldBeanAttributes() {
        BeanAttributes<Charlie> attributes = extension.getProducedCharlieAttributes();
        assertNotNull(attributes);
        assertEquals(ApplicationScoped.class, attributes.getScope());
        verifyName(attributes, "charlie");
        assertTrue(attributes.isAlternative()); // because charlie producer is
        assertTrue(attributes.isNullable());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, Charlie.class, CharlieInterface.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), AlphaStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), CharlieQualifier.class, Named.class, Any.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "ab"), @SpecAssertion(section = "11.5.10", id = "bb") })
    public void testInterceptorBeanAttributes() {
        BeanAttributes<BravoInterceptor> attributes = extension.getBravoInterceptorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, BravoInterceptor.class));
        assertTrue(attributes.getStereotypes().isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "ac"), @SpecAssertion(section = "11.5.10", id = "bb") })
    public void testDecoratorBeanAttributes() {
        BeanAttributes<BravoDecorator> attributes = extension.getBravoDecoratorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());

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

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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.synthetic;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ForwardingBeanAttributes;
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
public class ProcessBeanAttributesFiredForSyntheticBeanTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ProcessBeanAttributesFiredForSyntheticBeanTest.class)
                .withClass(ForwardingBeanAttributes.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Bicycle.class.getName()).up())
                .withExtensions(BicycleExtension.class, ModifyingExtension.class).build();
    }

    @Inject
    BicycleExtension bicycleExtension;

    @Inject
    ModifyingExtension modifyingExtension;

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.22", id = "a"), @SpecAssertion(section = "11.5.10", id = "aa"),
            @SpecAssertion(section = "11.5.10", id = "bb"), @SpecAssertion(section = "11.5.10", id = "bc"),
            @SpecAssertion(section = "11.5.6", id = "bc") })
    public void testProcessBeanAttributesFired() {

        assertTrue(bicycleExtension.isVetoed());
        assertTrue(modifyingExtension.isModified());

        BeanAttributes<Bicycle> attributesBeforeRegistering = bicycleExtension.getBicycleAttributesBeforeRegistering();
        assertEquals(attributesBeforeRegistering.getScope(), ApplicationScoped.class);
        assertTrue(typeSetMatches(attributesBeforeRegistering.getStereotypes(), FooStereotype.class));
        assertTrue(typeSetMatches(attributesBeforeRegistering.getTypes(), Object.class, Vehicle.class, Bicycle.class));
        assertTrue(annotationSetMatches(attributesBeforeRegistering.getQualifiers(), FooQualifier.class, Any.class));
        assertFalse(attributesBeforeRegistering.isAlternative());
        assertTrue(attributesBeforeRegistering.isNullable());

        BeanAttributes<Bicycle> attributesBeforeModifying = modifyingExtension.getBicycleAttributesBeforeModifying();
        assertEquals(attributesBeforeModifying.getScope(), ApplicationScoped.class);
        assertTrue(typeSetMatches(attributesBeforeModifying.getStereotypes(), FooStereotype.class));
        assertTrue(typeSetMatches(attributesBeforeModifying.getTypes(), Object.class, Vehicle.class, Bicycle.class));
        assertTrue(annotationSetMatches(attributesBeforeModifying.getQualifiers(), FooQualifier.class, Any.class));
        assertFalse(attributesBeforeModifying.isAlternative());
        assertTrue(attributesBeforeModifying.isNullable());

        Set<Bean<Bicycle>> beans = getBeans(Bicycle.class, AnyLiteral.INSTANCE);
        assertEquals(beans.size(), 1);
        Bean<Bicycle> bean = beans.iterator().next();
        assertEquals(bean.getScope(), RequestScoped.class);
        assertTrue(typeSetMatches(bean.getStereotypes(), BarStereotype.class));
        assertTrue(typeSetMatches(bean.getTypes(), Object.class, Bicycle.class));
        assertTrue(annotationSetMatches(bean.getQualifiers(), FooQualifier.class, Any.class));
        assertTrue(bean.isAlternative());
        assertTrue(bean.isNullable());
    }
}

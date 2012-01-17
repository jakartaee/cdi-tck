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
package org.jboss.cdi.tck.tests.extensions.beanManager.bean;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
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
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * TODO This test needs verification.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
// SHRINKWRAP-369
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class SyntheticBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanTest.class)
                .withClasses(ForwardingBeanAttributes.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(SimpleInterceptor.class.getName())
                                .up().createDecorators().clazz(VehicleDecorator.class.getName()).up())
                .withExtension(BeanExtension.class).build();
    }

    @Inject
    @Large
    Office office;

    @Inject
    @Hungry
    Lion lion;

    @Inject
    @Hungry
    Tiger tiger;

    @Inject
    FireTruck truck;

    @Inject
    SerializableOffice serializableOffice;

    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testRegisteredBean() {
        Bean<Office> bean = getUniqueBean(Office.class, Large.Literal.INSTANCE);
        assertEquals(bean.getInjectionPoints().size(), 3);
        for (InjectionPoint ip : bean.getInjectionPoints()) {
            assertEquals(bean, ip.getBean());
        }
        testOffice(bean);
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testSerializableBean() {
        @SuppressWarnings("unchecked")
        Bean<Office> bean = (Bean<Office>) getCurrentManager().resolve(
                getCurrentManager().getBeans(SerializableOffice.class, AnyLiteral.INSTANCE));
        assertTrue(bean instanceof PassivationCapable);
        testOffice(bean);
    }

    @Test
    @SpecAssertion(section = "9.3", id = "a")
    // TODO verify assertions
    public void testSyntheticBeanIntercepted() {
        assertTrue(office.intercepted());
        assertTrue(serializableOffice.intercepted());
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "b")
    public void testSyntheticProducerField() {
        assertNotNull(lion);
        lion.foo();
        Bean<Lion> bean = getUniqueBean(Lion.class, Hungry.Literal.INSTANCE);
        assertTrue(bean.getQualifiers().contains(Hungry.Literal.INSTANCE));
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "b")
    public void testSyntheticProducerMethod() {
        assertNotNull(tiger);
        tiger.foo();
        Bean<Tiger> bean = getUniqueBean(Tiger.class, Hungry.Literal.INSTANCE);
        assertTrue(bean.getQualifiers().contains(Hungry.Literal.INSTANCE));
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testSyntheticDecorator() {
        assertTrue(truck.decorated());
    }

    private void testOffice(Bean<Office> bean) {
        Office.reset();
        CreationalContext<Office> ctx = getCurrentManager().createCreationalContext(bean);
        Office office = (Office) bean.create(ctx);
        assertNotNull(office);
        assertNotNull(office.getConstructorEmployee());
        assertNotNull(office.getFieldEmployee());
        assertNotNull(office.getInitializerEmployee());
        assertTrue(office.isPostConstructCalled());
        bean.destroy(office, ctx);
        assertTrue(Office.isPreDestroyCalled());
    }
}

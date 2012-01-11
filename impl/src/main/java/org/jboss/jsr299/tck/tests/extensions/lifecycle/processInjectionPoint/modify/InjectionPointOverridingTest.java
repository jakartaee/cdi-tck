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
package org.jboss.jsr299.tck.tests.extensions.lifecycle.processInjectionPoint.modify;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.NewLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.util.ForwardingInjectionPoint;
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
public class InjectionPointOverridingTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InjectionPointOverridingTest.class)
                .withClasses(ForwardingInjectionPoint.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createDecorators().clazz(AnimalDecorator.class.getName())
                                .up()).withExtension(ModifyingExtension.class).build();
    }

    @Inject
    InjectingBean bean;

    @Inject
    @Fast
    Hound hound;

    @Inject
    @Lazy
    Dog dog;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "bb"),
            @SpecAssertion(section = "11.5.7", id = "c") })
    public void testOverridingFieldInjectionPoint() {
        assertTrue(bean.getDog() instanceof Hound);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "bb"),
            @SpecAssertion(section = "11.5.7", id = "c") })
    public void testDelegateInjectionPoint() {
        assertNotNull(hound);
        assertTrue(hound.decorated());
        assertNotNull(dog);
        assertTrue(dog.decorated());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "bb"),
            @SpecAssertion(section = "11.5.7", id = "c") })
    public void testNewInjectionPointDiscovered() {
        assertEquals(getBeans(Cat.class, NewLiteral.INSTANCE).size(), 1);
        assertNotNull(bean.getCat());
        assertNotNull(bean.getCat().getBean());
        assertEquals(bean.getCat().getBean().getScope(), Dependent.class);
        assertNull(bean.getCat().getBean().getName());
    }
}

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
package org.jboss.cdi.tck.tests.lookup.manager.provider;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.deployment.Bar;
import org.jboss.cdi.tck.tests.extensions.alternative.deployment.Foo;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
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
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class CdiProviderIncontainerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(CdiProviderIncontainerTest.class)
                .withClasses(Alpha.class, MarkerObtainerWar.class, Foo.class, Marker.class,
                        AfterDeploymentValidationObserver.class)
                .withExtension(AfterDeploymentValidationObserver.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Alpha.class.getName()).up())
                .withBeanLibrary(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Bravo.class.getName()).up(),
                        Bravo.class, MarkerObtainerBda1.class, Bar.class)
                .withBeanLibrary(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Charlie.class.getName()).up(),
                        Charlie.class, MarkerObtainerBda2.class, Baz.class)
                .withLibrary(MarkerObtainerNonBda.class, NonBdaAfterDeploymentValidationObserver.class).build();
    }

    @Inject
    AfterDeploymentValidationObserver bdaExtension;

    @Inject
    NonBdaAfterDeploymentValidationObserver nonbdaExtension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.1", id = "a"), @SpecAssertion(section = "12.1", id = "f") })
    public void testAccessingBeanManager() {

        // War itself
        assertNotNull(bdaExtension.getBeanManager());
        assertEquals(bdaExtension.getBeanManager(), getCurrentManager());

        assertEquals(MarkerObtainerWar.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerWar.getBeans(Marker.class).iterator().next().getBeanClass(), Alpha.class);
        assertEquals(MarkerObtainerWar.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerWar.getBeans(Bar.class).size(), 1);
        // BDA 1
        assertEquals(MarkerObtainerBda1.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerBda1.getBeans(Marker.class).iterator().next().getBeanClass(), Bravo.class);
        assertEquals(MarkerObtainerBda1.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerBda1.getBeans(Bar.class).size(), 1);
        // BDA 2
        assertEquals(MarkerObtainerBda2.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerBda2.getBeans(Marker.class).iterator().next().getBeanClass(), Charlie.class);
        assertEquals(MarkerObtainerBda2.getBeans(Baz.class).size(), 1);
        assertEquals(MarkerObtainerBda1.getBeans(Bar.class).size(), 1);

        // non-bda
        assertNotNull(nonbdaExtension.getBeanManager());
        assertEquals(nonbdaExtension.getBeanManager().getBeans(Marker.class).size(), 0);
        assertEquals(MarkerObtainerNonBda.getBeans(Marker.class).size(), 0);
        assertEquals(MarkerObtainerNonBda.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerNonBda.getBeans(Bar.class).size(), 1);
        assertEquals(MarkerObtainerNonBda.getBeans(Baz.class).size(), 1);
    }

}

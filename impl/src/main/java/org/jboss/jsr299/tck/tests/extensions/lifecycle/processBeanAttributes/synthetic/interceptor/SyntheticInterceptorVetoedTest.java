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
package org.jboss.jsr299.tck.tests.extensions.lifecycle.processBeanAttributes.synthetic.interceptor;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.InterceptionType;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.util.ForwardingBeanAttributes;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * First register two synthetic interceptor beans (AfterBeanDiscovery) and then veto one of them (ProcessBeanAttributes).
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
public class SyntheticInterceptorVetoedTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInterceptorVetoedTest.class)
                .withClasses(ForwardingBeanAttributes.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(ExternalInterceptor.class.getName()).up())
                .withExtension(ExternalInterceptorExtension.class).build();
    }

    @Inject
    ExternalInterceptorExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.10", id = "be"), @SpecAssertion(section = "11.5.2", id = "dd"),
            @SpecAssertion(section = "11.5.6", id = "bc") })
    public void testSyntheticInterceptorBeanCanBeVetoed() {
        assertTrue(extension.isTypeVetoed());
        assertTrue(extension.isBeanRegistered());
        assertTrue(extension.isBeanVetoed());
        // verify that one of these is vetoed (we do not know which one)
        int fooInterceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                FooBinding.Literal.INSTANCE).size();
        int barInterceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                BarBinding.Literal.INSTANCE).size();
        assertEquals(fooInterceptors + barInterceptors, 1);
    }

}

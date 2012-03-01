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
package org.jboss.cdi.tck.tests.deployment.initialization;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test application initialization lifecycle.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ApplicationInitializationLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationInitializationLifecycleTest.class)
                .withExtension(LifecycleMonitoringExtension.class).build();
    }

    @Inject
    Foo foo;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "12.2", id = "b"), @SpecAssertion(section = "12.2", id = "c"),
            @SpecAssertion(section = "12.2", id = "da"), @SpecAssertion(section = "12.2", id = "f"),
            @SpecAssertion(section = "12.2", id = "g"), @SpecAssertion(section = "12.2", id = "ga"),
            @SpecAssertion(section = "12.2", id = "h") })
    public void testInitialization() {

        foo.ping();

        // Test lifecycle phases occured
        assertTrue(LifecycleMonitoringExtension.createdAt > 0l);
        assertTrue(LifecycleMonitoringExtension.beforeBeanDiscoveryObservedAt > 0l);
        assertTrue(LifecycleMonitoringExtension.beanDiscoveryObservedAt > 0l);
        assertTrue(LifecycleMonitoringExtension.afterBeanDiscoveryObservedAt > 0l);
        assertTrue(LifecycleMonitoringExtension.afterDeploymentValidationObservedAt > 0l);
        assertTrue(Bar.injectionPerformedAt > 0l);
        assertTrue(Foo.pingPerformedAt > 0l);

        // Test lifecycle phases sequence
        List<Long> sequence = new ArrayList<Long>();
        // Extension registration
        sequence.add(LifecycleMonitoringExtension.createdAt);
        // BeforeBeanDiscovery
        sequence.add(LifecycleMonitoringExtension.beforeBeanDiscoveryObservedAt);
        // Bean discovery
        sequence.add(LifecycleMonitoringExtension.beanDiscoveryObservedAt);
        // AfterBeanDiscovery
        sequence.add(LifecycleMonitoringExtension.afterBeanDiscoveryObservedAt);
        // Validating bean dependencies and specialization - currently no portable way how to test
        // AfterDeploymentValidation
        sequence.add(LifecycleMonitoringExtension.afterDeploymentValidationObservedAt);
        // Inject any enums declaring injection points
        sequence.add(Bar.injectionPerformedAt);
        // Processing requests
        sequence.add(Foo.pingPerformedAt);

        List<Long> ascSequence = new ArrayList<Long>();
        ascSequence.addAll(sequence);
        // Sort asc
        Collections.sort(ascSequence);

        // See java.util.List.equals(Object)
        assertTrue(sequence.equals(ascSequence));
    }
}

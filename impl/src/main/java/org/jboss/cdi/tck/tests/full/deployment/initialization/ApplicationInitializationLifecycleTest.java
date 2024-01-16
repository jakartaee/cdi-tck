/*
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
package org.jboss.cdi.tck.tests.full.deployment.initialization;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZATION;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
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
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ApplicationInitializationLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationInitializationLifecycleTest.class)
                .withExtension(LifecycleMonitoringExtension.class).build();
    }

    @Inject
    Foo foo;

    @Test
    @SpecAssertions({ @SpecAssertion(section = INITIALIZATION, id = "b"), @SpecAssertion(section = INITIALIZATION, id = "c"),
            @SpecAssertion(section = INITIALIZATION, id = "ja"), @SpecAssertion(section = INITIALIZATION, id = "f"),
            @SpecAssertion(section = INITIALIZATION, id = "g"), @SpecAssertion(section = INITIALIZATION, id = "h"),
            @SpecAssertion(section = INITIALIZATION, id = "i"), @SpecAssertion(section = INITIALIZATION, id = "j") })
    public void testInitialization() {

        foo.ping();

        // Test lifecycle phases sequence
        List<String> correctSequenceData = new ArrayList<String>();
        // Extension registration
        correctSequenceData.add(LifecycleMonitoringExtension.class.getName());
        // BeforeBeanDiscovery
        correctSequenceData.add(BeforeBeanDiscovery.class.getName());
        // Bean discovery
        correctSequenceData.add(ProcessAnnotatedType.class.getName());
        // AfterTypeDiscovery
        correctSequenceData.add(AfterTypeDiscovery.class.getName());
        // AfterBeanDiscovery
        correctSequenceData.add(AfterBeanDiscovery.class.getName());
        // Validating bean dependencies and specialization - currently no portable way how to test
        // AfterDeploymentValidation
        correctSequenceData.add(AfterDeploymentValidation.class.getName());
        // Processing requests
        correctSequenceData.add(Foo.class.getName());

        assertEquals(ActionSequence.getSequenceData(), correctSequenceData);
    }
}

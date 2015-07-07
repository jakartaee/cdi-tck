/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015 Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.extensions.observer.priority;

import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;

import java.util.Arrays;

import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test ordering of container lifecycle events.
 *
 * @author Mark Paluch
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0 EDR1")
public class ExtensionObserverOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionObserverOrderingTest.class)
                .withExtension(PrioritizedExtensionEvents.class).build();
    }

    @Test
    @SpecAssertion(section = INIT_EVENTS, id = "e")
    public void testEventOrdering() {
        String[] expectedArray = new String[] { AfterDeploymentValidation.class.getSimpleName(), PrioritizedExtensionEvents.A,
                ProcessAnnotatedType.class.getSimpleName(), PrioritizedExtensionEvents.B, PrioritizedExtensionEvents.C,
                ProcessBeanAttributes.class.getSimpleName(),
                BeforeBeanDiscovery.class.getSimpleName() };
        ActionSequence.assertSequenceDataEquals(Arrays.asList(expectedArray));
    }
}

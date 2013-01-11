/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.extensions.annotated.delivery;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PAT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
@Test(groups = INTEGRATION)
public class WithAnnotationsTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(WithAnnotationsTest.class)
                .withClasses(Baby.class, BeforeBeanDiscoveryObserver.class, Desired.class, Egg.class, Falcon.class, Hen.class,
                        Hummingbird.class, Chicken.class, Phoenix.class, ProcessAnnotatedTypeObserver.class, Raven.class,
                        Sparrow.class, Turkey.class, Wanted.class).withLibrary(Griffin.class)
                .withExtensions(ProcessAnnotatedTypeObserver.class, BeforeBeanDiscoveryObserver.class).build();
    }

    @Inject
    ProcessAnnotatedTypeObserver processAnnotatedTypeObserver;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PAT, id = "fa"), @SpecAssertion(section = PAT, id = "fb"),
            @SpecAssertion(section = PAT, id = "fc"), @SpecAssertion(section = PAT, id = "fd"),
            @SpecAssertion(section = PAT, id = "fe"), @SpecAssertion(section = PAT, id = "ff") })
    public void testDelivery() {

        List<Class<?>> processedDesiredAnWantedTypes = processAnnotatedTypeObserver.getProcessedDesiredAndWantedTypes();
        assertFalse(processedDesiredAnWantedTypes.isEmpty());
        assertEquals(processedDesiredAnWantedTypes.size(), 6);
        assertTrue(processedDesiredAnWantedTypes.contains(Hummingbird.class));
        assertTrue(processedDesiredAnWantedTypes.contains(Falcon.class));
        assertTrue(processedDesiredAnWantedTypes.contains(Turkey.class));
        assertTrue(processedDesiredAnWantedTypes.contains(Sparrow.class));
        assertTrue(processedDesiredAnWantedTypes.contains(Hen.class));
        assertTrue(processedDesiredAnWantedTypes.contains(Griffin.class));

        List<Class<?>> processedDesiredTypes = processAnnotatedTypeObserver.getProcessedDesiredTypes();
        assertFalse(processedDesiredTypes.isEmpty());
        assertEquals(processedDesiredTypes.size(), 3);
        assertTrue(processedDesiredTypes.contains(Hummingbird.class));
        assertTrue(processedDesiredTypes.contains(Turkey.class));
        assertTrue(processedDesiredTypes.contains(Sparrow.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PAT, id = "fa"), @SpecAssertion(section = PAT, id = "g") })
    public void testDeliveryMetaAnnotation() {
        List<Class<?>> processedTypes = processAnnotatedTypeObserver.getProcessedMetaAnnotationTypes();
        assertFalse(processedTypes.isEmpty());
        assertEquals(processedTypes.size(), 2);
        assertTrue(processedTypes.contains(Chicken.class));
        assertTrue(processedTypes.contains(Hummingbird.class));
    }

}

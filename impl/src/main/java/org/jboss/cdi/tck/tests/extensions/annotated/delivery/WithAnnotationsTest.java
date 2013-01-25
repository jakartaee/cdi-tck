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
public class WithAnnotationsTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(WithAnnotationsTest.class)
                .withClasses(Baby.class, BeforeBeanDiscoveryObserver.class, Desired.class, Egg.class, Bird.class, Pirate.class,
                        Falcon.class, BatFalcon.class, Hen.class, Hummingbird.class, BeeHummingbird.class, Chicken.class,
                        RubberChicken.class, Phoenix.class, ProcessAnnotatedTypeObserver.class, Raven.class, Sparrow.class,
                        Jack.class, Turkey.class, OcellatedTurkey.class, Wanted.class, MetaAnnotation.class, Griffin.class)
                .withExtensions(ProcessAnnotatedTypeObserver.class, BeforeBeanDiscoveryObserver.class).build();
    }

    @Inject
    ProcessAnnotatedTypeObserver processAnnotatedTypeObserver;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PAT, id = "fa"), @SpecAssertion(section = PAT, id = "fb"),
            @SpecAssertion(section = PAT, id = "fc"), @SpecAssertion(section = PAT, id = "fd"),
            @SpecAssertion(section = PAT, id = "fe"), @SpecAssertion(section = PAT, id = "ff") })
    public void testDelivery() {

        // Annotated with @Desired or @Wanted
        List<Class<?>> desiredWantedTypes = processAnnotatedTypeObserver.getProcessedDesiredAndWantedTypes();
        assertFalse(desiredWantedTypes.isEmpty());
        assertEquals(desiredWantedTypes.size(), 12);
        // type-level
        assertTrue(desiredWantedTypes.contains(Bird.class));
        assertTrue(desiredWantedTypes.contains(Hummingbird.class));
        assertTrue(desiredWantedTypes.contains(BeeHummingbird.class));
        // member-level
        assertTrue(desiredWantedTypes.contains(Falcon.class));
        assertTrue(desiredWantedTypes.contains(BatFalcon.class));
        assertTrue(desiredWantedTypes.contains(Griffin.class));
        assertTrue(desiredWantedTypes.contains(Hen.class));
        assertTrue(desiredWantedTypes.contains(RubberChicken.class));
        assertTrue(desiredWantedTypes.contains(Turkey.class));
        assertTrue(desiredWantedTypes.contains(OcellatedTurkey.class));
        assertTrue(desiredWantedTypes.contains(Jack.class));
        assertTrue(desiredWantedTypes.contains(Sparrow.class));

        // Annotated with @Desired only
        List<Class<?>> desiredTypes = processAnnotatedTypeObserver.getProcessedDesiredTypes();
        assertFalse(desiredTypes.isEmpty());
        assertEquals(desiredTypes.size(), 7);
        // type-level
        assertTrue(desiredTypes.contains(Bird.class));
        assertTrue(desiredTypes.contains(Hummingbird.class));
        assertTrue(desiredTypes.contains(BeeHummingbird.class));
        // member-level
        assertTrue(desiredTypes.contains(Turkey.class));
        assertTrue(desiredTypes.contains(OcellatedTurkey.class));
        assertTrue(desiredTypes.contains(Sparrow.class));
        assertTrue(desiredTypes.contains(Jack.class));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PAT, id = "fa"), @SpecAssertion(section = PAT, id = "fc"),
            @SpecAssertion(section = PAT, id = "g") })
    public void testDeliveryMetaAnnotation() {
        List<Class<?>> metaTypes = processAnnotatedTypeObserver.getProcessedMetaAnnotationTypes();
        assertFalse(metaTypes.isEmpty());
        assertEquals(metaTypes.size(), 5);
        assertTrue(metaTypes.contains(Chicken.class));
        assertTrue(metaTypes.contains(Hen.class));
        assertTrue(metaTypes.contains(RubberChicken.class));
        assertTrue(metaTypes.contains(Hummingbird.class));
        assertTrue(metaTypes.contains(BeeHummingbird.class));
    }

}

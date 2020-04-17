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

import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.util.Assert.assertTypeListMatches;
import static org.testng.Assert.assertFalse;

import jakarta.inject.Inject;

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
@SpecVersion(spec = "cdi", version = "2.0")
public class WithAnnotationsTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(WithAnnotationsTest.class)
                .withClasses(Baby.class, BeforeBeanDiscoveryObserver.class, Desired.class, Egg.class, Bird.class, Pirate.class,
                        Falcon.class, BatFalcon.class, AplomadoFalcon.class, Hen.class, Hummingbird.class, BeeHummingbird.class, Chicken.class,
                        RubberChicken.class, Phoenix.class, ProcessAnnotatedTypeObserver.class, Raven.class, Sparrow.class,
                        Jack.class, Turkey.class, OcellatedTurkey.class, Wanted.class, MetaAnnotation.class, Griffin.class)
                .withExtensions(ProcessAnnotatedTypeObserver.class, BeforeBeanDiscoveryObserver.class).build();
    }

    @Inject
    ProcessAnnotatedTypeObserver processAnnotatedTypeObserver;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fa"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fb"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fc"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fd"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fe"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ff") })
    public void testDelivery() {

        // Annotated with @Desired or @Wanted
        assertTypeListMatches(processAnnotatedTypeObserver.getProcessedDesiredAndWantedTypes(),
                // type-level
                Bird.class, Hummingbird.class, BeeHummingbird.class,
                // member-level
                Falcon.class, BatFalcon.class, Griffin.class, Hen.class, RubberChicken.class, Turkey.class,
                OcellatedTurkey.class, Jack.class, Sparrow.class, AplomadoFalcon.class);

        // Annotated with @Desired only
        assertTypeListMatches(processAnnotatedTypeObserver.getProcessedDesiredTypes(),
        // type-level
                Bird.class, Hummingbird.class, BeeHummingbird.class,
                // member-level
                Turkey.class, OcellatedTurkey.class, Sparrow.class, Jack.class);

        assertFalse(processAnnotatedTypeObserver.getProcessedRequestScopeTypes().contains(AplomadoFalcon.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fa"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fc"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "g") })
    public void testDeliveryMetaAnnotation() {
        assertTypeListMatches(processAnnotatedTypeObserver.getProcessedMetaAnnotationTypes(), Chicken.class, Hen.class,
                RubberChicken.class, Hummingbird.class, BeeHummingbird.class);
    }

}

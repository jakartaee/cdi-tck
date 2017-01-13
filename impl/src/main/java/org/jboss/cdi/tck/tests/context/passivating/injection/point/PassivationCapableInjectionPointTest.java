/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.passivating.injection.point;

import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_INJECTION_POINTS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class PassivationCapableInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PassivationCapableInjectionPointTest.class).build();
    }

    @Inject
    Spoon spoon;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "b"),
            @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "d"),
            @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "f") })
    public void testPassivationCapableInjectionPoints() {
        assertNotNull(spoon.getMeal1());
        assertNotNull(spoon.getMeal2());
        assertNotNull(spoon.getMeal3());
        assertEquals(spoon.getMeal1().getId(), spoon.getMeal2().getId());
        assertEquals(spoon.getMeal1().getId(), spoon.getMeal3().getId());
    }

}

/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context.passivating.transientreference;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_INJECTION_POINTS;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class TransientReferenceParameterTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransientReferenceParameterTest.class).build();
    }

    @Inject
    Spoon spoon;

    @Test(groups = CDI_FULL)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "ad")
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "af")
    @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "c")
    @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "e")
    public void testParamInjectionPoints() {
        assertNotNull(spoon);
        assertTrue(spoon.isConstructorParamInjected());
        assertTrue(spoon.isInitParamInjected());
    }
}

/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.named;

import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_NAME;
import static org.jboss.cdi.tck.cdi.Sections.NAMED_AT_INJECTION_POINT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class NamedAtInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NamedAtInjectionPointTest.class).build();
    }

    @Inject
    FishingNet fishingNet;

    @Inject
    Pike pike;

    @Test
    @SpecAssertions({ @SpecAssertion(section = NAMED_AT_INJECTION_POINT, id = "a"), @SpecAssertion(section = DEFAULT_NAME, id = "fa") })
    public void testFieldNameUsedAsBeanName() {
        // Managed bean
        assertNotNull(fishingNet);
        assertEquals(fishingNet.getCarp().ping(), Integer.valueOf(1));
        // Producer method
        assertNotNull(pike);
        assertNotNull(pike.getDaphnia());
        assertEquals(pike.getDaphnia().getName(), DaphniaProducer.NAME);
    }

}

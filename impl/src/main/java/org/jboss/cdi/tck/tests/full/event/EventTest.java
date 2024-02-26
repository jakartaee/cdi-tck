/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.event;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_METHOD_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class EventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZATION, id = "cc"),
            @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa") })
    public void testObserverCalledOnSpecializedBeanOnly() {
        Shop.observers.clear();
        getCurrentManager().getEvent().select(Delivery.class).fire(new Delivery());
        // FarmShop specializes Shop
        assertEquals(Shop.observers.size(), 1);
        assertEquals(Shop.observers.iterator().next(), FarmShop.class.getName());
    }
}

/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.finalMethod;

import static org.jboss.cdi.tck.cdi.Sections.UNPROXYABLE;
import static org.testng.Assert.assertEquals;

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
public class PrivateFinalMethodTest extends AbstractTest {

    @Inject
    WhaleCovey whaleCovey;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(PrivateFinalMethodTest.class).withClasses(Whale.class, WhaleCovey.class)
                .build();
    }

    @Test
    @SpecAssertion(section = UNPROXYABLE, id = "bda")
    public void testClassWithPrivateFinalMethodCanBeProxied() {
        whaleCovey.ping();
        assertEquals(getCurrentManager().getBeans(Whale.class).size(), 1);
    }
}

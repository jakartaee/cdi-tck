/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.fires.sync;

import static org.jboss.cdi.tck.cdi.Sections.FIRING_EVENTS_SYNCHRONOUSLY;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@SpecVersion(spec = "cdi", version = "2.0")
public class FireSyncEventTest extends AbstractTest {

    @Inject
    Helper helper;

    @Inject
    Event<Letter> event;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(FireSyncEventTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = FIRING_EVENTS_SYNCHRONOUSLY, id = "e"),
            @SpecAssertion(section = FIRING_EVENTS_SYNCHRONOUSLY, id = "f") })
    public void testSyncObservesCalledInSameThread() {

        event.fire(new Letter());
        helper.addThreadID((int) Thread.currentThread().getId());
        Assert.assertEquals(helper.getCounter(), 2);
        Assert.assertEquals(helper.getThreadIDs().size(), 1);
    }

}

/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.async.executor;

import static org.jboss.cdi.tck.cdi.Sections.EVENT;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.NotificationOptions;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class FireAsyncWithCustomExecutorTest extends AbstractTest {

    @Inject
    Event<Message> event;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(FireAsyncWithCustomExecutorTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "ee") })
    public void testCustomExecutor() throws InterruptedException {
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        NotificationOptions notificationOptions = NotificationOptions.ofExecutor(new CustomExecutor());
        event.fireAsync(new Message(), notificationOptions).thenAccept(queue::add);

        Message message = queue.poll(2, TimeUnit.SECONDS);
        assertNotNull(message);
        assertTrue(MessageObserver.observed.get());
        assertTrue(CustomExecutor.executed.get());
    }

}

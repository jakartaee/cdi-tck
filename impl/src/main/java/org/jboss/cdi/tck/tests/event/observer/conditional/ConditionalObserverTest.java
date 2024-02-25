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
package org.jboss.cdi.tck.tests.event.observer.conditional;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONDITIONAL_OBSERVER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_METHOD_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Reception;
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
public class ConditionalObserverTest extends AbstractTest {

    @Inject
    Event<AsyncConditionalEvent> asyncConditionalEventEvent;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ConditionalObserverTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa"),
            @SpecAssertion(section = CONDITIONAL_OBSERVER_METHODS, id = "a") })
    public void testConditionalObserver() {

        Event<ConditionalEvent> conditionalEvent = getCurrentManager().getEvent().select(ConditionalEvent.class);
        conditionalEvent.fire(new ConditionalEvent());
        // Should not be notified since bean is not instantiated yet
        assert !WidowSpider.isNotified();

        // Now instantiate the bean and fire another event
        WidowSpider bean = getContextualReference(WidowSpider.class);
        assert bean != null;
        // Must invoke a method to really create the instance
        assert !bean.isInstanceNotified();
        conditionalEvent.fire(new ConditionalEvent());
        assert WidowSpider.isNotified() && bean.isInstanceNotified();

    }

    @Test
    @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa")
    public void testObserverMethodInvokedOnReturnedInstanceFromContext() {

        RecluseSpider spider = getContextualReference(RecluseSpider.class);
        spider.setWeb(new Web());
        getCurrentManager().getEvent().select(ConditionalEvent.class).fire(new ConditionalEvent());
        assert spider.isInstanceNotified();
        assert spider.getWeb().getRings() == 1;
    }

    @Test
    @SpecAssertion(section = CONDITIONAL_OBSERVER_METHODS, id = "c")
    public void testNotifyEnumerationContainsNotifyValues() {
        assert Reception.values().length == 2;
        List<String> notifyValueNames = new ArrayList<String>();
        for (Reception value : Reception.values()) {
            notifyValueNames.add(value.name());
        }

        assert notifyValueNames.contains("IF_EXISTS");
        assert notifyValueNames.contains("ALWAYS");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "bca")
    public void testConditionalObserverMethodNotInvokedIfNoActiveContext() {

        Tarantula.reset();
        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();
        Tarantula tarantula = getContextualReference(Tarantula.class);
        tarantula.ping();

        Event<TarantulaEvent> tarantulaEvent = getCurrentManager().getEvent().select(TarantulaEvent.class);
        try {
            // Instance exists but there is no context active for its scope
            setContextInactive(requestContext);
            tarantulaEvent.fire(new TarantulaEvent());
            // Observer method not called
            assertFalse(Tarantula.isNotified());
        } finally {
            setContextActive(requestContext);
        }
        // Context is active now
        tarantulaEvent.fire(new TarantulaEvent());
        assertTrue(Tarantula.isNotified());
    }

    @Test
    @SpecAssertion(section = CONDITIONAL_OBSERVER_METHODS, id = "a")
    public void testAsyncConditionalObserver() throws InterruptedException {
        BlockingQueue<AsyncConditionalEvent> queue = new LinkedBlockingQueue<>();
        asyncConditionalEventEvent.fireAsync(new AsyncConditionalEvent()).thenAccept(queue::offer);
        AsyncConditionalEvent event = queue.poll(2, TimeUnit.SECONDS);
        assertFalse(AsyncConditionalObserver.IsNotified().get());

        AsyncConditionalObserver observer = getContextualReference(AsyncConditionalObserver.class);
        assertNotNull(observer);
        observer.ping();

        asyncConditionalEventEvent.fireAsync(new AsyncConditionalEvent()).thenAccept(queue::offer);
        event = queue.poll(2, TimeUnit.SECONDS);
        assertTrue(AsyncConditionalObserver.IsNotified().get());
    }

}

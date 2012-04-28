/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.context.jms.LogStore.LogMessage;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test for buil-in request and application scopes that shall be active during any message delivery to a MessageListener for a
 * JMS topic or queue obtained from the Java EE component environment.
 * 
 * Note that basic JMS configuration is required for this test.
 * 
 * FIXME verify this test after AS7-3107 is resolved
 * 
 * @author Martin Kouba
 */
@Test(groups = { JAVAEE_FULL, JMS })
@SpecVersion(spec = "cdi", version = "20091101")
public class MessageListenerContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(MessageListenerContextTest.class)
                .withClasses(LoggerService.class, LogStore.class, SimpleMessageProducer.class, AbstractMessageListener.class,
                        QueueMessageListener.class, TopicMessageListener.class, SimpleMessageConsumer.class).build();
    }

    @Inject
    SimpleMessageConsumer consumer;

    @Inject
    SimpleMessageProducer producer;

    @Inject
    LogStore store;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6.7.1", id = "ge"), @SpecAssertion(section = "6.7.1", id = "i"),
            @SpecAssertion(section = "6.7.3", id = "de"), @SpecAssertion(section = "6.7.3", id = "df") })
    public void testRequestScopeActiveDuringMessageDelivery() throws Exception {

        consumer.init();

        producer.sendQueueMessage();
        producer.sendTopicMessage();

        // Wait for async processing
        new Timer().setDelay(2000l).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return AbstractMessageListener.processedMessages.get() >= 2;
            }
        }).start();

        List<LogMessage> logMessages = store.getLogMessages();
        assertEquals(logMessages.size(), 2);
        LogMessage msg1 = logMessages.get(0);
        assertEquals(msg1.getText(), SimpleMessageProducer.class.getName());
        LogMessage msg2 = logMessages.get(1);
        assertEquals(msg2.getText(), SimpleMessageProducer.class.getName());
        // Test that each message delivery has its own request context
        assertFalse(msg1.getServiceId().equals(msg2.getServiceId()));
    }

}

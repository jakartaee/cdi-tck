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
package org.jboss.jsr299.tck.tests.context.jms;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * Simple JMS consumer.
 * 
 * @author Martin Kouba
 */
@ApplicationScoped
public class SimpleMessageConsumer {

    @Resource(mappedName = "java:/ConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/test")
    private static Queue queue;

    @Resource(mappedName = "java:/topic/test")
    private static Topic topic;

    @Inject
    private QueueMessageListener queueMessageListener;

    @Inject
    private TopicMessageListener topicMessageListener;

    private Connection connection = null;

    public void init() {

        Session session = null;
        MessageConsumer messageConsumer = null;

        try {

            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Queue consumer
            messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(queueMessageListener);

            // Topic consumer
            messageConsumer = session.createConsumer(topic);
            messageConsumer.setMessageListener(topicMessageListener);

            connection.start();

        } catch (JMSException e) {
            throw new RuntimeException("Cannot init message consumer");
        }
    }

    @PreDestroy
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
            }
        }
    }

}

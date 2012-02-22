package org.jboss.cdi.tck.tests.context.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class AbstractMessageListener implements MessageListener {

    public static AtomicInteger processedMessages = new AtomicInteger(0);

    @Inject
    private LoggerService loggerService;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                loggerService.log(((TextMessage) message).getText());
            } catch (JMSException e) {
                // Cannot consume message
            } finally {
                processedMessages.incrementAndGet();
            }
        } else {
            throw new IllegalArgumentException("Unsupported message type");
        }
    }

    public static void resetProcessedMessages() {
        processedMessages.set(0);
    }

}

package org.jboss.cdi.tck.tests.context.request.event.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class AbstractMessageListener implements MessageListener {

    public static AtomicInteger processedMessages = new AtomicInteger(0);

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            processedMessages.incrementAndGet();
        } else {
            throw new IllegalArgumentException("Unsupported message type");
        }
    }

    public static void resetProcessedMessages() {
        processedMessages.set(0);
    }

}

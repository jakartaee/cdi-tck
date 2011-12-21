package org.jboss.jsr299.tck.tests.context.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class AbstractMessageListener implements MessageListener {

    @Inject
    private LoggerService loggerService;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                loggerService.log(((TextMessage) message).getText());
            } catch (JMSException e) {
                // Cannot consume message
            }
        } else {
            throw new IllegalArgumentException("Unsupported message type");
        }
    }

}

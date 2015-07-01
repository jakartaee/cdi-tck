package org.jboss.cdi.tck.tests.extensions.observer.priority;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.interceptor.Interceptor;

import org.jboss.cdi.tck.tests.event.observer.priority.Priority;

/**
 * @author Mark Paluch
 */
public class PrioritizedExtensionEvents implements Extension {

    private List<String> notificationOrder = new ArrayList<String>();

    <T> void processBeanEarly(@Observes @Priority(Interceptor.Priority.LIBRARY_BEFORE) ProcessBean<T> processBean) {
        notificationOrder.add("processBeanEarly");
    }

    <T> void processBeanSomewhereInTheMiddle(@Observes ProcessBean<T> processBean) {
        notificationOrder.add("processBeanSomewhereInTheMiddle");
    }

    <T> void processBeanLate(@Observes @Priority(Interceptor.Priority.LIBRARY_AFTER) ProcessBean<T> processBean) {
        notificationOrder.add("processBeanLate");
    }

	public List<String> getNotificationOrder() {
		return notificationOrder;
	}
}

package org.jboss.cdi.tck.tests.build.compatible.extensions.changeObserverQualifier;

public class MyEvent {
    final String payload;

    public MyEvent(String payload) {
        this.payload = payload;
    }
}

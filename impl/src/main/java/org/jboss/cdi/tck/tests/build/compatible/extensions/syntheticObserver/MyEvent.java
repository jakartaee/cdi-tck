package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserver;

public class MyEvent {
    final String payload;

    MyEvent(String payload) {
        this.payload = payload;
    }
}

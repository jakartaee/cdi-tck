package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserver;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyService {
    @Inject
    Event<MyEvent> unqualifiedEvent;

    @Inject
    @MyQualifier
    Event<MyEvent> qualifiedEvent;

    void fireEvent() {
        unqualifiedEvent.fire(new MyEvent("Hello World"));
        qualifiedEvent.fire(new MyEvent("Hello Special"));
    }
}

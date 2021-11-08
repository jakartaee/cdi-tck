package org.jboss.cdi.tck.tests.build.compatible.extensions.changeObserverQualifier;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyProducer {
    @Inject
    Event<MyEvent> unqualified;

    @Inject
    @MyQualifier
    Event<MyEvent> qualified;

    void produce() {
        unqualified.fire(new MyEvent("unqualified"));
        qualified.fire(new MyEvent("qualified"));
    }
}

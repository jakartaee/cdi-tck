package org.jboss.cdi.tck.tests.build.compatible.extensions.changeObserverQualifier;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class MyConsumer {
    static final Set<String> events = new HashSet<>();

    void consume(@Observes MyEvent event) {
        events.add(event.payload);
    }

    void noConsume(@Observes MyEvent event) {
        events.add("must-not-happen-" + event.payload);
    }
}

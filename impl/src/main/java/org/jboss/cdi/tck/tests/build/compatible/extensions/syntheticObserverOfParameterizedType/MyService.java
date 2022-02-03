package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserverOfParameterizedType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MyService {
    @Inject
    Event<List<MyData>> event;

    void fireEvent() {
        event.fire(List.of(new MyData("Hello"), new MyData("World")));
        event.fire(List.of(new MyData("Hello"), new MyData("again")));
    }
}

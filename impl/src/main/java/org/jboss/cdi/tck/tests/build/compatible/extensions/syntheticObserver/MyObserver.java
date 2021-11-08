package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserver;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticObserver;
import jakarta.enterprise.inject.spi.EventContext;

import java.util.ArrayList;
import java.util.List;

public class MyObserver implements SyntheticObserver<MyEvent> {
    static final List<String> observed = new ArrayList<>();

    @Override
    public void observe(EventContext<MyEvent> event, Parameters params) throws Exception {
        observed.add(event.getEvent().payload + " with " + params.get("name", String.class));
    }
}

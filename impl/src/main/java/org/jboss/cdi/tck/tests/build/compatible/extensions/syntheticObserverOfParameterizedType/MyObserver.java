package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserverOfParameterizedType;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticObserver;
import jakarta.enterprise.inject.spi.EventContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyObserver implements SyntheticObserver<List<MyData>> {
    static final List<String> observed = new ArrayList<>();

    @Override
    public void observe(EventContext<List<MyData>> event, Parameters params) throws Exception {
        observed.add(event.getEvent()
                .stream()
                .map(it -> it.payload)
                .collect(Collectors.joining(" ")));
    }
}

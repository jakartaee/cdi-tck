package org.jboss.cdi.tck.tests.implementation.producer.method.broken.parameterAnnotatedObservesAsync;

import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.inject.Produces;

public class BrokenProducer {

    @Produces
    public String produce(@ObservesAsync String message) {
        return "test";
    }
}

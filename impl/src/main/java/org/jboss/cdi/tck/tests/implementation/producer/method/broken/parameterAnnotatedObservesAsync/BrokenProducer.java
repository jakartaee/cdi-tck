package org.jboss.cdi.tck.tests.implementation.producer.method.broken.parameterAnnotatedObservesAsync;

import javax.enterprise.event.ObservesAsync;
import javax.ws.rs.Produces;

public class BrokenProducer {

    @Produces
    public String produce(@ObservesAsync String message) {
        return "test";
    }
}

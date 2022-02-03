package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;

@Dependent
public class MyServiceFoo implements MyService {
    @Override
    public String hello() {
        return "foo";
    }

    void init(@Observes Startup event) {
    }
}

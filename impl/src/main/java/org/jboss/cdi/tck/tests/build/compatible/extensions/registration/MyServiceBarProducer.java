package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class MyServiceBarProducer {
    @Produces
    @Dependent
    @MyQualifier
    public MyServiceBar produce() {
        return new MyServiceBar();
    }
}

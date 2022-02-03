package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import jakarta.enterprise.context.Dependent;

@Dependent
public class MyServiceFoo implements MyService {
    @Override
    public String hello() {
        return "foo";
    }
}

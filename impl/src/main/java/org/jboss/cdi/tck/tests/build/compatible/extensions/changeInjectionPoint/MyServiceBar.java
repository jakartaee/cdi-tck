package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import jakarta.enterprise.context.Dependent;

@Dependent
@MyQualifier
public class MyServiceBar implements MyService {
    @Override
    public String hello() {
        return "bar";
    }
}

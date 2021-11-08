package org.jboss.cdi.tck.tests.build.compatible.extensions.customQualifier;

public class MyServiceFoo implements MyService {
    public String hello() {
        return "foo";
    }
}

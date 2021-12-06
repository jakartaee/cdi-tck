package org.jboss.cdi.tck.tests.build.compatible.extensions.inspectAnnotatedSubtypes;

public class MyServiceBaz implements MyService {
    @Override
    @MyAnnotation
    public String hello() {
        return "baz";
    }
}

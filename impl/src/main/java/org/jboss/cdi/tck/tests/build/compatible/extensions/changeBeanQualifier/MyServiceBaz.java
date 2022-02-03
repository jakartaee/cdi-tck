package org.jboss.cdi.tck.tests.build.compatible.extensions.changeBeanQualifier;

public class MyServiceBaz implements MyService {
    @Override
    public String hello() {
        throw new UnsupportedOperationException();
    }
}

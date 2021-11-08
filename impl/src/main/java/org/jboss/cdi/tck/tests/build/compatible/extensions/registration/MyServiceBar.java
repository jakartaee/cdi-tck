package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

// intentionally not a bean, to test that producer-based bean is processed
public class MyServiceBar implements MyService {
    @Override
    public String hello() {
        return "bar";
    }
}

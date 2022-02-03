package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

@MyCustomStereotype
public class MyService {
    public String hello() {
        return "Hello!";
    }
}

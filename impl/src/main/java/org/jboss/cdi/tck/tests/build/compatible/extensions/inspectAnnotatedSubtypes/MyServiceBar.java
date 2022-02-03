package org.jboss.cdi.tck.tests.build.compatible.extensions.inspectAnnotatedSubtypes;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyServiceBar implements MyService {
    @Override
    public String hello() {
        return "bar";
    }
}

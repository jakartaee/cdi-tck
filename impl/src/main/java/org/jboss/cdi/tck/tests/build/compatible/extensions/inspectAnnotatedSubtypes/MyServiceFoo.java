package org.jboss.cdi.tck.tests.build.compatible.extensions.inspectAnnotatedSubtypes;

import jakarta.enterprise.context.ApplicationScoped;

@MyAnnotation
@ApplicationScoped
public class MyServiceFoo implements MyService {
    @Override
    public String hello() {
        return "foo";
    }
}

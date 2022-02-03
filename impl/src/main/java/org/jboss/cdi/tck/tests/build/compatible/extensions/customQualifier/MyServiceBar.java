package org.jboss.cdi.tck.tests.build.compatible.extensions.customQualifier;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MyCustomQualifier("this should be ignored, the value member should be treated as @Nonbinding")
public class MyServiceBar implements MyService {
    public String hello() {
        return "bar";
    }
}

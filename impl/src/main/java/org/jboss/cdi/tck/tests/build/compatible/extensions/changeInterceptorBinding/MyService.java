package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyService {

    public String hello() {
        return "Hello!";
    }
}

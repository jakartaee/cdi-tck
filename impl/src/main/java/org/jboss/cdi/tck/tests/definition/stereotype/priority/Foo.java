package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Foo {

    public String ping() {
        return Foo.class.getSimpleName();
    }
}

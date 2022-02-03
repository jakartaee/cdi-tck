package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Bar {

    public String ping() {
        return Bar.class.getSimpleName();
    }
}

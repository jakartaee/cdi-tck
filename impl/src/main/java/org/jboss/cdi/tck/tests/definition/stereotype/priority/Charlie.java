package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Charlie {

    public String ping() {
        return Charlie.class.getSimpleName();
    }
}

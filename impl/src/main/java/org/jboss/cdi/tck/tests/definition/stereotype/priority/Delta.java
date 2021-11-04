package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Delta {

    public String ping() {
        return Delta.class.getSimpleName();
    }
}

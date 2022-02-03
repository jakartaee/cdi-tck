package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
// alternative and priority gained via stereotype
@AlternativePriorityStereotype
public class BarExtended extends Bar {

    public String ping() {
        return BarExtended.class.getSimpleName();
    }
}

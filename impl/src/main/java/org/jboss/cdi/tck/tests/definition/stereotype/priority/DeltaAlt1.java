package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@Priority(199)
public class DeltaAlt1 extends Delta{

    public String ping() {
        return DeltaAlt1.class.getSimpleName();
    }
}

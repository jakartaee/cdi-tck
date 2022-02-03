package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@Priority(1)
@PriorityStereotype // stereotype has @Priority(100)
public class CharlieAltStereotype extends Charlie {

    public String ping() {
        return CharlieAltStereotype.class.getSimpleName();
    }
}

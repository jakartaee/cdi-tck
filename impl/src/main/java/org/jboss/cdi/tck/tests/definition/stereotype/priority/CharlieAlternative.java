package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@Priority(50)
public class CharlieAlternative extends Charlie {

    public String ping() {
        return CharlieAlternative.class.getSimpleName();
    }
}

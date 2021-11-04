package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@Priority(1)
// enabled alternative, should be overriden by BazAlternative2
public class BazAlternative extends Baz {

    public String ping() {
        return BazAlternative.class.getSimpleName();
    }
}

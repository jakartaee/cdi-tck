package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@PriorityStereotype
public class BazAlternative2 extends Baz {

    public String ping() {
        return BazAlternative2.class.getSimpleName();
    }
}

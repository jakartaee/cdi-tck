package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@AnotherPriorityStereotype // this will give it @Priority(200)
public class DeltaAlt2 extends Delta {

    public String ping() {
        return DeltaAlt2.class.getSimpleName();
    }
}

package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities.inherited;

import org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited.DumbStereotype;

@DumbStereotype
public class FooAncestor {

    public String ping() {
        return FooAncestor.class.getSimpleName();
    }

}

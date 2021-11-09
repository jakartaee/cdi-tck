package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.Delta;

@Dependent
@Alternative
@AnotherPriorityStereotype // this will result in a DefinitionException due to two conflicting values
public class SomeOtherBean extends Delta {

    public String ping() {
        return SomeOtherBean.class.getSimpleName();
    }
}

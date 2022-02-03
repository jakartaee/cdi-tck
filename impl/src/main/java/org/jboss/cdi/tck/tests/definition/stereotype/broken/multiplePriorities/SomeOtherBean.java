package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
@AnotherPriorityStereotype // this will result in a DefinitionException due to two conflicting values
public class SomeOtherBean {

    public String ping() {
        return SomeOtherBean.class.getSimpleName();
    }
}

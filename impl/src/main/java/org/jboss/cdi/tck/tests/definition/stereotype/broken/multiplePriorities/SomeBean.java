package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype;

@Dependent
@Alternative
@PriorityStereotype
@PriorityStereotype2
public class SomeBean {
}

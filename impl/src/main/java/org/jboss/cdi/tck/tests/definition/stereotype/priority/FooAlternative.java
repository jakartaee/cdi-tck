package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
// enabled through priority from stereotype
@PriorityStereotype
public class FooAlternative extends Foo {

    public String ping() {
        return FooAlternative.class.getSimpleName();
    }
}

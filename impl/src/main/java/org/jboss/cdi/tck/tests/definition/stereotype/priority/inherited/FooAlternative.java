package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
public class FooAlternative extends Foo {

    public String ping() {
        return FooAlternative.class.getSimpleName();
    }
}

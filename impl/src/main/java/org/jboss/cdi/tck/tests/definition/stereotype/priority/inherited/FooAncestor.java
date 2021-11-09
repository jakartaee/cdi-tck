package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

@DumbStereotype
public class FooAncestor {

    public String ping() {
        return FooAncestor.class.getSimpleName();
    }

}

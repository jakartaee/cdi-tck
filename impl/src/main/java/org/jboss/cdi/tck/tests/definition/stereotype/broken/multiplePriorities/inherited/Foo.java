package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities.inherited;

@AnotherDumbStereotype
public class Foo extends FooAncestor {

    public String ping() {
        return Foo.class.getSimpleName();
    }
}

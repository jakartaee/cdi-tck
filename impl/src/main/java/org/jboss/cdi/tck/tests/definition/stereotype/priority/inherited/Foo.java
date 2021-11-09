package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

public class Foo extends FooAncestor {

    public String ping() {
        return Foo.class.getSimpleName();
    }
}

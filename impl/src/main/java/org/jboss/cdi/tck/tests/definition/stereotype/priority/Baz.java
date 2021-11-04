package org.jboss.cdi.tck.tests.definition.stereotype.priority;

public class Baz {

    public String ping() {
        return Baz.class.getSimpleName();
    }
}

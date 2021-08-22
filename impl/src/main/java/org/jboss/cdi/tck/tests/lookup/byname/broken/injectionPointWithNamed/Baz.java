package org.jboss.cdi.tck.tests.lookup.byname.broken.injectionPointWithNamed;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Baz {

    public Baz(String name) {
    }

}

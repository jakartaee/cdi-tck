package org.jboss.cdi.tck.tests.full.extensions.configurators.bean.alternativePriority;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Foo {
    public String ping() {
        return "foo";
    }
}

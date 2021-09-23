package org.jboss.cdi.tck.tests.full.extensions.configurators.bean.alternativePriority;

import jakarta.enterprise.inject.Vetoed;

@Vetoed
public class Bar extends Foo {
    public String ping() {
        return "bar";
    }
}

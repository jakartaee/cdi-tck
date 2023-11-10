package org.jboss.cdi.tck.tests.alternative.selection;

import jakarta.enterprise.inject.Vetoed;

@Vetoed
public class Delta {

    String s;

    public Delta(String s) {
        this.s = s;
    }

    public String ping() {
        return s;
    }
}

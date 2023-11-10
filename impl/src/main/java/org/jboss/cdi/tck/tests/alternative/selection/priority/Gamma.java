package org.jboss.cdi.tck.tests.alternative.selection.priority;

public class Gamma {

    private String s;

    public Gamma(String s) {
        this.s = s;
    }

    public String ping() {
        return s;
    }
}

package org.jboss.cdi.tck.tests.alternative.selection.priority;

public class Delta {

    private String s;

    public Delta(String s) {
        this.s = s;
    }

    public String ping() {
        return s;
    }
}

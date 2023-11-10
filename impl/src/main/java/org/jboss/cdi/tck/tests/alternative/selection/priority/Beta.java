package org.jboss.cdi.tck.tests.alternative.selection.priority;

public class Beta {

    private String s;

    public Beta(String s) {
        this.s = s;
    }

    public String ping() {
        return s;
    }
}

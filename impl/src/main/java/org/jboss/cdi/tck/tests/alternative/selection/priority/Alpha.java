package org.jboss.cdi.tck.tests.alternative.selection.priority;

public class Alpha {

    private String s;

    public Alpha(String s) {
        this.s = s;
    }

    public String ping() {
        return s;
    }
}

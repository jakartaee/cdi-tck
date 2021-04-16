package org.jboss.shrinkwrap.api;

public enum BeanDiscoveryMode {

    NONE("none"), ANNOTATED("annotated"), ALL("all");

    private final String value;

    private BeanDiscoveryMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

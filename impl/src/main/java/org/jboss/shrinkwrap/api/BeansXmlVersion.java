package org.jboss.shrinkwrap.api;

public enum BeansXmlVersion {

    v30("3.0"), v20("2.0"), v11("1.1");

    private final String value;

    private BeansXmlVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

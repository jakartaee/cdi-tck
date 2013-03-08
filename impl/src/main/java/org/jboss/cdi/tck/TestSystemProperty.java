package org.jboss.cdi.tck;

/**
 *
 * @author Martin Kouba
 */
public enum TestSystemProperty {

    EXCLUDE_DUMMY("cdiTckExcludeDummy", "true");

    TestSystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}

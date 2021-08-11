package org.jboss.cdi.tck.test.spi.warsp;

import jakarta.inject.Inject;
import org.jboss.cdi.tck.test.spi.sp.VersionInfo;

public class SomeBean {
    @VersionInfo
    static String VERSION = "1.0";

    @Inject
    private String name;

    public String getName() {
        return name;
    }
}

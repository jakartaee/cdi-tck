package org.jboss.cdi.tck.test.spi.broken.warsp;

import jakarta.inject.Inject;
import org.jboss.cdi.tck.test.spi.broken.BadBean;
import org.jboss.cdi.tck.test.spi.sp.VersionInfo;

/**
 * This bean should trigger a deployment exception when the SourceProcessor is run
 */
@BadBean
public class SomeBean {
    @VersionInfo
    static String VERSION = "1.0";

    @Inject
    private String name;

    public String getName() {
        return name;
    }
}

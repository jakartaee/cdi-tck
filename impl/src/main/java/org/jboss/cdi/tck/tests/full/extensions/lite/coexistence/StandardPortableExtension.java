package org.jboss.cdi.tck.tests.full.extensions.lite.coexistence;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/**
 * Standard PE with one dummy method to verify that PE are executed alongside BCE
 */
public class StandardPortableExtension implements Extension {

    public static boolean INVOKED = false;

    public void bbd(@Observes BeforeBeanDiscovery bbd) {
        INVOKED = true;
    }
}

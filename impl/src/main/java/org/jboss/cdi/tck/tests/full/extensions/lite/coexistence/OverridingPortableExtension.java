package org.jboss.cdi.tck.tests.full.extensions.lite.coexistence;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * This PE overrides {@link OverridenBuildCompatibleExtension} and should be executed instead
 */
public class OverridingPortableExtension implements Extension {

    public static int TIMES_INVOKED = 0;

    public void pat(@Observes ProcessAnnotatedType<DummyBean> pat) {
        TIMES_INVOKED++;
    }

    public void bbd(@Observes BeforeBeanDiscovery bbd) {
        TIMES_INVOKED++;
    }

    public void abd(@Observes AfterBeanDiscovery abd) {
        TIMES_INVOKED++;
    }
}

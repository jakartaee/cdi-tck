package org.jboss.cdi.tck.tests.full.extensions.lite.coexistence;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.lang.model.declarations.ClassInfo;

/**
 * This is a standard BCE and should be normally executed
 */
public class StandardBuildCompatibleExtension implements BuildCompatibleExtension {

    public static int TIMES_INVOKED = 0;

    @Discovery
    public void discovery() {
        TIMES_INVOKED++;
    }

    @Enhancement(types = DummyBean.class)
    public void enhancement(ClassInfo c) {
        TIMES_INVOKED++;
    }

    @Registration(types = DummyBean.class)
    public void registration(BeanInfo b) {
        TIMES_INVOKED++;
    }

    @Synthesis
    public void synthesis() {
        TIMES_INVOKED++;
    }

    @Validation
    public void validation() {
        TIMES_INVOKED++;
    }
}

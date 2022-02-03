package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import org.jboss.test.audit.annotations.SpecVersion;

@SpecVersion(spec = "cdi", version = "4.0")
public class EnhancementNoParamExtension implements BuildCompatibleExtension {

    @Enhancement(types = SomeBean.class)
    public void enhance() {
        // no-op deployment should fail
    }
}

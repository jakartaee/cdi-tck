package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Types;

public class EnhancementOnlyTypesExtension implements BuildCompatibleExtension {

    @Enhancement(types = SomeBean.class)
    public void enhance(Types types) {
        // no-op deployment should fail
    }
}

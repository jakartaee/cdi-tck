package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.lang.model.declarations.ClassInfo;

public class EnhancementMultipleParamsExtension implements BuildCompatibleExtension {

    @Enhancement(types = SomeBean.class)
    public void enhance(ClassConfig cc, ClassInfo ci, Messages msg) {
        // no-op deployment should fail
    }
}

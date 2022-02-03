package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.MethodConfig;
import jakarta.enterprise.lang.model.declarations.FieldInfo;

public class EnhancementMultipleParamsExtension2 implements BuildCompatibleExtension {

    @Enhancement(types = SomeBean.class)
    public void enhance(ClassConfig cc, MethodConfig mc, FieldInfo fi) {
        // no-op deployment should fail
    }
}

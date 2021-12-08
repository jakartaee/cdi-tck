package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;

public class EnhancementOnlyMessagesExtension implements BuildCompatibleExtension {

    @Enhancement(types = SomeBean.class)
    public void enhance(Messages msg) {
        // no-op deployment should fail
    }
}

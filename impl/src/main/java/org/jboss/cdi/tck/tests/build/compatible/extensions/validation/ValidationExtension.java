package org.jboss.cdi.tck.tests.build.compatible.extensions.validation;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.Validation;

public class ValidationExtension implements BuildCompatibleExtension {
    @Validation
    public void validate(Messages msg) {
        msg.error("Deployment should fail");
    }
}

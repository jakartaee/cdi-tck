package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.Registration;

public class RegistrationOnlyMessagesExtension implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register(Messages m) {
        // no-op, deployment should fail
    }
}

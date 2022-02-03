package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Registration;

public class RegistrationNoParamExtension implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register() {
        // no-op, deployment should fail
    }
}

package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Types;

public class RegistrationOnlyTypesExtension implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register(Types t) {
        // no-op, deployment should fail
    }
}

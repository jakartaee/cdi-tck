package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ObserverInfo;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Types;

public class RegistrationMultipleParamsExtension2 implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register(ObserverInfo oi, Types t, BeanInfo bi) {
        // no-op, deployment should fail
    }
}

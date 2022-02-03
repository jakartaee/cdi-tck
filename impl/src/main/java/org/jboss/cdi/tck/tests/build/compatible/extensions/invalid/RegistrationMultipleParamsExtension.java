package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InterceptorInfo;
import jakarta.enterprise.inject.build.compatible.spi.Registration;

public class RegistrationMultipleParamsExtension implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register(BeanInfo bi, InterceptorInfo ii) {
        // no-op, deployment should fail
    }
}

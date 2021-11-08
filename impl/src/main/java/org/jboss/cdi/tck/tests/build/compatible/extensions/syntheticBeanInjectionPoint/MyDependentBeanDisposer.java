package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionPoint;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class MyDependentBeanDisposer implements SyntheticBeanDisposer<MyDependentBean> {
    static InjectionPoint lookedUp = null;

    @Override
    public void dispose(MyDependentBean instance, Instance<Object> lookup, Parameters params) {
        lookedUp = lookup.select(InjectionPoint.class).get();
    }
}

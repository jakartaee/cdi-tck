package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionPoint;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class MyApplicationScopedBeanCreator implements SyntheticBeanCreator<MyApplicationScopedBean> {
    static InjectionPoint lookedUp = null;

    @Override
    public MyApplicationScopedBean create(Instance<Object> lookup, Parameters params) {
        lookedUp = lookup.select(InjectionPoint.class).get();
        return new MyApplicationScopedBean();
    }
}

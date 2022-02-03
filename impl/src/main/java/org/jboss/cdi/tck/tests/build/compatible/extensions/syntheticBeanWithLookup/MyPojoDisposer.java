package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer;

import java.util.concurrent.atomic.AtomicInteger;

public class MyPojoDisposer implements SyntheticBeanDisposer<MyPojo> {
    static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void dispose(MyPojo instance, Instance<Object> lookup, Parameters params) {
        counter.incrementAndGet();

        lookup.select(MyDependentBean.class).get();

        instance.destroy();
    }
}

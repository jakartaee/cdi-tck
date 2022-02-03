package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;

import java.util.concurrent.atomic.AtomicInteger;

public class MyPojoCreator implements SyntheticBeanCreator<MyPojo> {
    static final AtomicInteger counter = new AtomicInteger();

    @Override
    public MyPojo create(Instance<Object> lookup, Parameters params) {
        counter.incrementAndGet();

        lookup.select(MyDependentBean.class).get();

        return new MyPojo();
    }
}

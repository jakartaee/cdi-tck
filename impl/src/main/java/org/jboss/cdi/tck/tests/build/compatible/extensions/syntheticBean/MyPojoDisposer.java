package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer;

import java.util.HashSet;
import java.util.Set;

public class MyPojoDisposer implements SyntheticBeanDisposer<MyPojo> {
    static final Set<String> disposed = new HashSet<>();

    @Override
    public void dispose(MyPojo instance, Instance<Object> lookup, Parameters params) {
        disposed.add(instance.text);
    }
}

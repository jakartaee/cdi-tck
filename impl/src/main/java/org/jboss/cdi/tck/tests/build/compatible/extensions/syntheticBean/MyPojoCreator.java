package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class MyPojoCreator implements SyntheticBeanCreator<MyPojo> {
    @Override
    public MyPojo create(Instance<Object> lookup, Parameters params) {
        String name = params.get("name", String.class);
        MyComplexValue ann = params.get("data", MyComplexValue.class);

        InjectionPoint injectionPoint = lookup.select(InjectionPoint.class).get();
        if (injectionPoint.getQualifiers().stream().anyMatch(it -> it.annotationType().equals(MyQualifier.class))) {
            return new MyPojo("Hello @MyQualifier " + name, ann);
        }

        return new MyPojo("Hello " + name, ann);
    }
}

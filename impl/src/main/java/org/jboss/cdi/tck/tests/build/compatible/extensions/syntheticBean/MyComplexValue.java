package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyComplexValue {
    int number();

    MyEnum enumeration();

    Class<?> type();

    MySimpleValue nested();
}

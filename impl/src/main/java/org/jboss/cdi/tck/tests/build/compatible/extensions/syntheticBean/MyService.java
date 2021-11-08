package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class MyService {
    @Inject
    MyPojo unqualified;

    @Inject
    @MyQualifier
    MyPojo qualified;
}

package org.jboss.cdi.tck.tests.build.compatible.extensions.changeBeanQualifier;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class MyOtherService {
    @Inject
    MyService myService;
}

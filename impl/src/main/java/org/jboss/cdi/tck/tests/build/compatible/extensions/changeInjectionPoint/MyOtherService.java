package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class MyOtherService {
    @Inject
    MyService myService;
}

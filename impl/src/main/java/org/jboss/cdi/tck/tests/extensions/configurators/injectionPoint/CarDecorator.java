package org.jboss.cdi.tck.tests.extensions.configurators.injectionPoint;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.inject.Inject;

@Decorator
@Priority(2000)
public class CarDecorator implements Vehicle {

    @Inject
    // ip is turned to delegate ip in PIP
    public void initTank(Car car) {
    }
}

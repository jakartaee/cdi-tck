package org.jboss.cdi.tck.tests.full.extensions.configurators.injectionPoint;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.inject.Inject;

@Decorator
@Priority(2000)
public class CarDecorator implements Vehicle {

    @Inject
    // ip is turned to delegate ip in PIP
    public void initTank(Car car) {
    }
}

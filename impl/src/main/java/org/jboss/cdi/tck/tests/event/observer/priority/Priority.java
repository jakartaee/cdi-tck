package org.jboss.cdi.tck.tests.event.observer.priority;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Priority {
    int value();
}

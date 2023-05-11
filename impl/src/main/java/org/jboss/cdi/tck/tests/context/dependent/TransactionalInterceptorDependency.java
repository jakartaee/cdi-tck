package org.jboss.cdi.tck.tests.context.dependent;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class TransactionalInterceptorDependency {
    public static boolean destroyed = false;

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }
}

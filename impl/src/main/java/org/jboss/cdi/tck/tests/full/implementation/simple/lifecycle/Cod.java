package org.jboss.cdi.tck.tests.full.implementation.simple.lifecycle;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class Cod {

    private static boolean expcetionThrown = false;

    @PreDestroy
    public void destroyWithProblem() {
        expcetionThrown = true;
        throw new RuntimeException("Some error");
    }

    public void ping() {

    }

    public static boolean isExpcetionThrown() {
        return expcetionThrown;
    }
}

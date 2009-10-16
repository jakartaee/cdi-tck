package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.enterprise.inject.Produces;

public class TunaProducer {
    
    @Produces @Tame Tuna createQualifiedTuna() {
        return new Tuna() {
            public String getName() {
                return "qualifiedTuna";
            }
        };
    }
}

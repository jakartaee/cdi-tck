package org.jboss.cdi.tck.tests.definition.bean.types.illegal;

import jakarta.enterprise.inject.Produces;

public class EagleProducer<T> {

    @Produces
    @ProducedWithField
    public Eagle<T> eagle = new Eagle<>();

    @Produces
    @ProducedWithMethod
    public Eagle<T> createEagle(){
        return new Eagle<>();
    }
}

package org.jboss.cdi.tck.tests.implementation.producer.method.broken.parameterizedTypeWithTypeParameter;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

public class DoubleListProducer<T> {

    @Produces
    @RequestScoped
    public List<List<T>> create() {
        return new ArrayList<List<T>>();
    }

}

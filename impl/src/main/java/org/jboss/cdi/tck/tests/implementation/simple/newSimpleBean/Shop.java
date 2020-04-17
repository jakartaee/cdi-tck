package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

@SuppressWarnings("serial")
public class Shop implements Serializable {

    @Inject
    @New
    private Order newOrder;

    public Order getNewOrder() {
        return newOrder;
    }

}

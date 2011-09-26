package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import javax.enterprise.inject.New;
import javax.inject.Inject;

public class Shop implements Serializable {

    @Inject
    @New
    private Order newOrder;

    public Order getNewOrder() {
        return newOrder;
    }

}

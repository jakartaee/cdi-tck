package org.jboss.cdi.tck.tests.full.definition.bean.custom;

import jakarta.inject.Inject;

public class Bar {

    @Inject
    private Integer one;

    public int getOne() {
        return one;
    }

}

package org.jboss.cdi.tck.tests.definition.bean.custom;

import javax.inject.Inject;

public class Bar {

    @Inject
    private Integer one;

    public int getOne() {
        return one;
    }

}

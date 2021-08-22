package org.jboss.cdi.tck.tests.definition.bean.custom;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class Bar {

    @Inject
    private Integer one;

    public int getOne() {
        return one;
    }

}

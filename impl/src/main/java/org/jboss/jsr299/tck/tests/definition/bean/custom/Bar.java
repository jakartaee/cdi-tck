package org.jboss.jsr299.tck.tests.definition.bean.custom;

import java.io.Serializable;

import javax.inject.Inject;

public class Bar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Integer one;

    public int getOne() {
        return one;
    }

}

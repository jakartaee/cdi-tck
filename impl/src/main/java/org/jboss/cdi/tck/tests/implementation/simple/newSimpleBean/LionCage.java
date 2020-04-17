package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

public class LionCage {

    @Inject
    @New
    private Lion newLion;

    public Lion getNewLion() {
        return newLion;
    }

}

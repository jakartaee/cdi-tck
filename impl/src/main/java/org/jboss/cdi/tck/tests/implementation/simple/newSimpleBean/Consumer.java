package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

public class Consumer {

    @Inject
    private ExplicitContructorSimpleBean explicitConstructorBean;

    @Inject
    @New
    private ExplicitContructorSimpleBean newExplicitConstructorBean;

    @Inject
    private InitializerSimpleBean initializerSimpleBean;

    @Inject
    @New
    private InitializerSimpleBean newInitializerSimpleBean;

    public ExplicitContructorSimpleBean getExplicitConstructorBean() {
        return explicitConstructorBean;
    }

    public ExplicitContructorSimpleBean getNewExplicitConstructorBean() {
        return newExplicitConstructorBean;
    }

    public InitializerSimpleBean getInitializerSimpleBean() {
        return initializerSimpleBean;
    }

    public InitializerSimpleBean getNewInitializerSimpleBean() {
        return newInitializerSimpleBean;
    }

}

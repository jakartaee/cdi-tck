package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.enterprise.context.Dependent;
import jakarta.interceptor.Interceptors;

@Dependent
class Fish {
    @Interceptors(FishInterceptor.class)
    public String foo() {
        return "bar";
    }
}

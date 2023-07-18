package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.enterprise.context.Dependent;
import jakarta.interceptor.ExcludeClassInterceptors;
import jakarta.interceptor.Interceptors;

@Interceptors(DogInterceptor.class)
@Dependent
class Dog {
    public String foo() {
        return "bar";
    }
}

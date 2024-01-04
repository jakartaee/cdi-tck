package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

@Priority(Interceptor.Priority.APPLICATION + 400)
@ProductInterceptorBinding2
@Interceptor
public class ProductInterceptor1 {

    private static Set<Annotation> allBindings;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext) throws Exception {
        allBindings = invocationContext.getInterceptorBindings();
        return (1 + (Integer)invocationContext.proceed());
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }

}

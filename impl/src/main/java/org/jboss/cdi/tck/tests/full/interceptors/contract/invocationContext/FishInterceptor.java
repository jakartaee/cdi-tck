package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

public class FishInterceptor {
    private static Set<Annotation> allBindings;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        allBindings = ctx.getInterceptorBindings();
        return "fish: " + ctx.proceed();
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }
}

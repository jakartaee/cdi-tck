package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Set;

@Interceptor
@AroundConstructBinding1
@Priority(100)
public class AroundConstructInterceptor1 {
    private static Set<Annotation> allBindings;

    @AroundConstruct
    public Object intercept(InvocationContext ctx) throws Exception {
        allBindings = ctx.getInterceptorBindings();
        return ctx.proceed();
    }

    public static Set<Annotation> getAllBindings() {
        return allBindings;
    }
}

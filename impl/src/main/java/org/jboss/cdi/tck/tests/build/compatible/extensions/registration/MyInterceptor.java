package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@MyInterceptorBinding
@Interceptor
@Priority(1)
public class MyInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }
}

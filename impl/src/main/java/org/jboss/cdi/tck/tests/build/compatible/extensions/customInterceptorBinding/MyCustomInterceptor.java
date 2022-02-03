package org.jboss.cdi.tck.tests.build.compatible.extensions.customInterceptorBinding;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@MyCustomInterceptorBinding("something")
public class MyCustomInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return "Intercepted: " + ctx.proceed();
    }
}

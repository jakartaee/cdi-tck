package org.jboss.cdi.tck.tests.lookup.injectionpoint;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Priority(1)
@SomeBinding
public class LoggerInterceptor {

    public static String message;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        message = (String) ctx.proceed();
        return message;
    }

    public static void reset() {
        message = null;
    }
}

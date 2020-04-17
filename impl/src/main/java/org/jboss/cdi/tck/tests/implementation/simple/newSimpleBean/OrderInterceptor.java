package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Secure
@Transactional
public class OrderInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return true;
    }
}

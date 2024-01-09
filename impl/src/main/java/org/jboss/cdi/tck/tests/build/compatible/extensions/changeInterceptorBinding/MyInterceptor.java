package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@MyBinding("")
@Priority(1000)
public class MyInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        MyBinding binding = ctx.getInterceptorBinding(MyBinding.class);
        return "Intercepted(" + binding.value() + "): " + ctx.proceed();
    }
}

package org.jboss.jsr299.tck.tests.interceptors.definition.inheritance;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@GuardedByWoodpecker
public class WoodpeckerInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {

        Object target = ctx.getTarget();

        if (target instanceof Plant) {
            ((Plant) target).inspect(this.getClass().getName());
        }
        return ctx.proceed();
    }

}

package org.jboss.cdi.tck.tests.implementation.builtin.metadata.session;

import java.io.Serializable;

import jakarta.enterprise.inject.Intercepted;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import org.jboss.cdi.tck.tests.full.decorators.implementation.builtin.metadata.Frozen;

@SuppressWarnings("serial")
@jakarta.interceptor.Interceptor
@Frozen
public class YoghurtInterceptor implements Serializable {

    @Inject
    private Bean<org.jboss.cdi.tck.tests.full.decorators.implementation.builtin.metadata.YoghurtInterceptor> bean;
    @Inject
    private Interceptor<org.jboss.cdi.tck.tests.full.decorators.implementation.builtin.metadata.YoghurtInterceptor> interceptor;
    @Inject
    @Intercepted
    private Bean<?> interceptedBean;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return this;
    }

    public Bean<org.jboss.cdi.tck.tests.full.decorators.implementation.builtin.metadata.YoghurtInterceptor> getBean() {
        return bean;
    }

    public Interceptor<org.jboss.cdi.tck.tests.full.decorators.implementation.builtin.metadata.YoghurtInterceptor> getInterceptor() {
        return interceptor;
    }

    public Bean<?> getInterceptedBean() {
        return interceptedBean;
    }
}

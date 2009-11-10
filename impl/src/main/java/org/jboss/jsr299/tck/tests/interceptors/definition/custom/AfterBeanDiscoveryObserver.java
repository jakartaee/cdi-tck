package org.jboss.jsr299.tck.tests.interceptors.definition.custom;

import static javax.enterprise.inject.spi.InterceptionType.AROUND_INVOKE;
import static javax.enterprise.inject.spi.InterceptionType.AROUND_TIMEOUT;
import static javax.enterprise.inject.spi.InterceptionType.POST_ACTIVATE;
import static javax.enterprise.inject.spi.InterceptionType.POST_CONSTRUCT;
import static javax.enterprise.inject.spi.InterceptionType.PRE_DESTROY;
import static javax.enterprise.inject.spi.InterceptionType.PRE_PASSIVATE;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;


public class AfterBeanDiscoveryObserver implements Extension
{
   public static final CustomInterceptorImplementation POST_CONSTRUCT_INTERCEPTOR = new CustomInterceptorImplementation(POST_CONSTRUCT);
   public static final CustomInterceptorImplementation PRE_DESTROY_INTERCEPTOR = new CustomInterceptorImplementation(PRE_DESTROY);
   public static final CustomInterceptorImplementation POST_ACTIVATE_INTERCEPTOR = new CustomInterceptorImplementation(POST_ACTIVATE);
   public static final CustomInterceptorImplementation PRE_PASSIVATE_INTERCEPTOR = new CustomInterceptorImplementation(PRE_PASSIVATE);
   public static final CustomInterceptorImplementation AROUND_INVOKE_INTERCEPTOR = new CustomInterceptorImplementation(AROUND_INVOKE);
   public static final CustomInterceptorImplementation AROUND_TIMEOUT_INTERCEPTOR = new CustomInterceptorImplementation(AROUND_TIMEOUT);
   
   public void addInterceptors(@Observes AfterBeanDiscovery event) {
      event.addBean(POST_CONSTRUCT_INTERCEPTOR);
      event.addBean(PRE_DESTROY_INTERCEPTOR);
      event.addBean(POST_ACTIVATE_INTERCEPTOR);
      event.addBean(PRE_PASSIVATE_INTERCEPTOR);
      event.addBean(AROUND_INVOKE_INTERCEPTOR);
      event.addBean(AROUND_TIMEOUT_INTERCEPTOR);
   }
}

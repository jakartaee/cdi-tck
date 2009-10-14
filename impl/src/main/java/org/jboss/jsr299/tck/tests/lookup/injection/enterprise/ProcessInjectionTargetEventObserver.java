package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

public class ProcessInjectionTargetEventObserver implements Extension
{

   private static ProcessInjectionTarget<Farm> sessionBeanEvent = null; 
   private static ProcessInjectionTarget<FarmInterceptor> ejbInterceptorEvent = null; 
   
   public void observeSessionBean(@Observes ProcessInjectionTarget<Farm> event) {
      sessionBeanEvent = event;
   }
   
   public void observeEJBInterceptor(@Observes ProcessInjectionTarget<FarmInterceptor> event) {
      ejbInterceptorEvent = event;
   }

   public static ProcessInjectionTarget<Farm> getSessionBeanEvent()
   {
      return sessionBeanEvent;
   }

   public static ProcessInjectionTarget<FarmInterceptor> getEjbInterceptorEvent()
   {
      return ejbInterceptorEvent;
   }
}

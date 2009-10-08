package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
public class Farm implements FarmLocal
{
   @Inject private Sheep sheep;
   private boolean injectionPerformedCorrectly = false;

   @Interceptors(FarmInterceptor.class)
   public int getAnimalCount() {
      return 1;
   }
   
   @PostConstruct
   public void postConstruct() {
      injectionPerformedCorrectly = sheep != null;
   }

   public boolean isInjectionPerformedCorrectly()
   {
      return injectionPerformedCorrectly;
   }
}

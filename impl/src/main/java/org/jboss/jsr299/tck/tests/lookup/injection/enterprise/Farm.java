package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
public class Farm implements FarmLocal
{
   @Inject
   private Sheep sheepField;

   private boolean initializerCalledAfterInjection = false;
   private boolean injectionPerformedCorrectly = false;

   @Inject
   public void initialize(Sheep sheep)
   {
      initializerCalledAfterInjection = (sheepField != null) && (sheep != null);
   }

   @Interceptors(FarmInterceptor.class)
   public int getAnimalCount()
   {
      return 1;
   }

   @PostConstruct
   public void postConstruct()
   {
      injectionPerformedCorrectly = initializerCalledAfterInjection;
   }

   public boolean isInjectionPerformedCorrectly()
   {
      return injectionPerformedCorrectly;
   }
}

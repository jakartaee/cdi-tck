package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
public class Farm implements FarmLocal
{
   @Inject private Sheep sheepField;
   
   private boolean initializerCalled;
   private boolean injectionPerformedCorrectly = false;
   
   @Inject private void initialize(Sheep sheep)
   {
      assert sheepField != null;
      initializerCalled = true;
   }

   @Interceptors(FarmInterceptor.class)
   public int getAnimalCount() {
      return 1;
   }
   
   @PostConstruct
   public void postConstruct() {
      assert initializerCalled;
      injectionPerformedCorrectly = true;
   }

   public boolean isInjectionPerformedCorrectly()
   {
      return injectionPerformedCorrectly;
   }
}

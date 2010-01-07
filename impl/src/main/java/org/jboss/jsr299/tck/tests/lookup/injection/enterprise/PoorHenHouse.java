package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.inject.Inject;

public class PoorHenHouse extends HenHouse
{
   protected boolean initializerCalledAfterSuperclassInjection = false;

   @Inject
   public void initialize()
   {
      initializerCalledAfterSuperclassInjection = fox != null;
   }

   public boolean isInitializerCalledAfterSuperclassInjection()
   {
      return initializerCalledAfterSuperclassInjection;
   }
}

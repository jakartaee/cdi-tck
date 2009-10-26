package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless
public class MegaPoorHenHouse extends PoorHenHouse implements MegaPoorHenHouseLocal
{
   private boolean postConstructCalledAfterSuperclassInitializer = false;
   
   public Fox getFox()
   {
      return fox;
   }
   
   @PostConstruct
   public void postConstruct() {
      postConstructCalledAfterSuperclassInitializer = initializerCalledAfterSuperclassInjection;
   }

   public boolean isPostConstructCalledAfterSuperclassInitializer()
   {
      return postConstructCalledAfterSuperclassInitializer;
   }
   
   public boolean isInitializerCalledAfterSuperclassInjection()
   {
      return initializerCalledAfterSuperclassInjection;
   }
}

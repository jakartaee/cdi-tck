package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class InjectedSessionBean implements InjectedSessionBeanLocal
{
   @EJB
   private FarmLocal farm;
   private boolean initializerCalledAfterResourceInjection = false;

   public FarmLocal getFarm()
   {
      return farm;
   }
   
   @Inject
   public void initialize() {
      initializerCalledAfterResourceInjection = farm != null;
   }

   public boolean isInitializerCalledAfterResourceInjection()
   {
      return initializerCalledAfterResourceInjection;
   }
}

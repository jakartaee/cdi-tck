package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

@Stateless
@WebService
public class BirdFeederService implements FeederService
{
   @Inject
   private BeanManager jsr299Manager;

   private static boolean applicationScopeActive = false;

   @WebMethod
   public void refillFood()
   {
      if (jsr299Manager.getContext(ApplicationScoped.class).isActive())
      {
         applicationScopeActive = true;
      }
      else
      {
         applicationScopeActive = false;
      }
   }

   public boolean adequateFood()
   {
      return applicationScopeActive;
   }

}

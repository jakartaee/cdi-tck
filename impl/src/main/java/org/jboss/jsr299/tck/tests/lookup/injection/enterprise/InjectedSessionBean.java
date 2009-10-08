package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InjectedSessionBean implements InjectedSessionBeanLocal
{
   @EJB
   private FarmLocal farm;

   public FarmLocal getFarm()
   {
      return farm;
   }
}

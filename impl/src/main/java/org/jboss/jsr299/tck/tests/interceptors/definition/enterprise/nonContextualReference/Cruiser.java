package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise.nonContextualReference;

import javax.ejb.EJB;

public class Cruiser implements Ship
{

   @EJB
   MissileLocal missile;

   public void defend()
   {
      missile.fire();
   }
}

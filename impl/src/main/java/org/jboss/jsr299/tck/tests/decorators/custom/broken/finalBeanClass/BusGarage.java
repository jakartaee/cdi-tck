package org.jboss.jsr299.tck.tests.decorators.custom.broken.finalBeanClass;

import javax.inject.Inject;

class BusGarage
{
   @Inject
   private Bus bus;

   public Bus getBus()
   {
      return bus;
   }
}

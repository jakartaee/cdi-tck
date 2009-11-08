package org.jboss.jsr299.tck.tests.decorators.custom.broken.finalBeanClass;

import javax.decorator.Delegate;
import javax.inject.Inject;

class VehicleDecorator implements Vehicle
{
   @Inject @Delegate
   Vehicle delegate;

   public void start()
   {
      delegate.start();
   }
}

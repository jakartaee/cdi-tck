package org.jboss.jsr299.tck.tests.decorators.custom;

import javax.decorator.Delegate;
import javax.inject.Inject;

class VehicleDecorator implements Vehicle
{
   @Inject @Delegate
   private Vehicle delegate;

   public String start()
   {
      return delegate.start() + " and decorated.";
   }

   public String stop()
   {
      return delegate.stop() + " and decorated.";
   }
}

package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.enterprise.inject.Produces;

class Farm
{
   @SuppressWarnings("unused")
   @Produces
   private Milk milk = new Milk(true);
   
   @Produces
   public Cheese getCheese() {
      return new Cheese(true);
   }
}

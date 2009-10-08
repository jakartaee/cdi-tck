package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

class OakTree
{
   @Produces @Dependent public Acorn growAcorn()
   {
      return null;
   }
   
   @Produces @RequestScoped @Yummy public Pollen pollinate()
   {
      return null;
   }
}

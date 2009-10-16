package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

class Lays
{
   public @Produces @Null @RequestScoped PotatoChip makeChip()
   {       
      return null;
   }
}

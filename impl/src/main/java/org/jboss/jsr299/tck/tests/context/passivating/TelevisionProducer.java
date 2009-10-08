package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

class TelevisionProducer
{
   @Produces @SessionScoped Television getTelevision()
   {
      return new Television();
   }
}

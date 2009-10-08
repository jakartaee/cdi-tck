package org.jboss.jsr299.tck.tests.context.passivating.broken.passivatingProducerMethodWithNonPassivatingParameter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

class Peraseinajoki extends City
{

   @Produces @SessionScoped
   public Violation2 create(Violation reference)
   {
      return new Violation2();
   }

}

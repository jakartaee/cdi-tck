package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.enterprise.inject.Produces;

public class CowProducer
{
   
   @Produces @Noisy
   public Cow produceNoisyCow()
   {
      return new Cow();
   }
   

}

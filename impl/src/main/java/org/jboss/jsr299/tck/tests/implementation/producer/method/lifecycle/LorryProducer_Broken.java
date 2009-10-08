package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;

class LorryProducer_Broken
{
   
   public @Produces @Fail Lorry produceLorry() throws Exception
   {
      throw new Exception();
   }
   
}

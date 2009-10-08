package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterAnnotatedDisposes;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class SpiderProducer_Broken
{
   
   @Produces
   public String dispose(@Disposes String foo)
   {
      return "foo";
   }
   
}

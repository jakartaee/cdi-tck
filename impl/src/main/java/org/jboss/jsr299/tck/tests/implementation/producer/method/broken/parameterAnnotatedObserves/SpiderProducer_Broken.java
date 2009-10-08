package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterAnnotatedObserves;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

class SpiderProducer_Broken
{
   
   @Produces
   public String observe(@Observes String foo)
   {
      return "foo";
   }
   
}

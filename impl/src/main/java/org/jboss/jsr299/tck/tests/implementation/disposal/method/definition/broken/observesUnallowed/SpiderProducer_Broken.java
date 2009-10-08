package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.observesUnallowed;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class SpiderProducer_Broken
{

   @Produces
   public static Spider getSpider()
   {
      return new Spider();
   }

   public static void destorySpider(@Disposes @Observes Spider spider)
   {
   }

}

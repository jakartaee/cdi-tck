package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.producesUnallowed;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class SpiderProducer_Broken
{
   @Produces
   public static Spider getSpider()
   {
      return new Spider();
   }

   @Produces
   public static void destorySpider(@Disposes Spider spider)
   {
   }
}

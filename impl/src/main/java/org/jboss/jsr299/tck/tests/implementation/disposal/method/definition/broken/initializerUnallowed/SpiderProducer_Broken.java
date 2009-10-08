package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.initializerUnallowed;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

class SpiderProducer_Broken
{

   @Produces
   public static Spider getSpider()
   {
      return new Spider();
   }

   @Inject
   public static void destorySpider(@Disposes Spider spider)
   {
   }

}

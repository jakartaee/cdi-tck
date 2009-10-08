package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   
   private static Spider[] ALL_SPIDERS = { new Tarantula(), new LadybirdSpider(), new DaddyLongLegs() };
   
   @Produces public Spider[] getSpiders()
   {
      return ALL_SPIDERS;
   }
   
   @Produces public String[] getStrings()
   {
      return new String[0];
   }
   
}

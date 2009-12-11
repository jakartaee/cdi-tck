package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;

class FunnelWeaverSpiderProducer
{
   private static FunnelWeaver<Spider> spider;

   @Produces @Spidery
   public FunnelWeaver<Spider> getFunnelWeaverSpider = new FunnelWeaver<Spider>();

   public FunnelWeaverSpiderProducer()
   {
      spider = this.getFunnelWeaverSpider;
   }
   
   public static FunnelWeaver<Spider> getSpider()
   {
      return spider;
   }
}

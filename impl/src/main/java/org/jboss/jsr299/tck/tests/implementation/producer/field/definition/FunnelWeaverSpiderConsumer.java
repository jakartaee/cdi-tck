package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.inject.Inject;

class FunnelWeaverSpiderConsumer
{
   @Inject @Spidery
   private FunnelWeaver<Spider> injectedSpider;

   public FunnelWeaver<Spider> getInjectedSpider()
   {
      return injectedSpider;
   }
}

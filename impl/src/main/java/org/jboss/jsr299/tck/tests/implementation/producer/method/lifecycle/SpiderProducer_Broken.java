package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

class SpiderProducer_Broken
{

   @Produces
   @RequestScoped
   @Request
   public Spider getRequestScopedSpider()
   {
      return null;
   }

}

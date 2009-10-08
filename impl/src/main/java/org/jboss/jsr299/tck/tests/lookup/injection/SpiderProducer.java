package org.jboss.jsr299.tck.tests.lookup.injection;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   
   @Produces public int getWolfSpiderSize()
   {
      return 4;
   }
   
}

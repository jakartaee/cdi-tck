package org.jboss.jsr299.tck.tests.definition.bean;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   
   @Produces @AnimalStereotype public WolfSpider produceWolfSpider()
   {
      return new WolfSpider();
   }
   
   @Produces @Tame public Animal makeASpider()
   {
      return new WolfSpider();
   }
   
   @Produces public int getWolfSpiderSize()
   {
      return 4;
   }

}

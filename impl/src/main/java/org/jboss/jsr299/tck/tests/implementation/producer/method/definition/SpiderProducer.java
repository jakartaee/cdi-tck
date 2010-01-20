package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

class SpiderProducer
{
   
   private static Spider[] ALL_SPIDERS = { new Tarantula(), new LadybirdSpider(), new DaddyLongLegs() };
   
   @Produces @Tame public Tarantula produceTameTarantula()
   {
      return new DefangedTarantula(0);
   }
   
   @Produces @Deadliest public Tarantula producesDeadliestTarantula(@Tame Tarantula tameTarantula, 
         Tarantula tarantula)
   {      
      return tameTarantula.getDeathsCaused() >= tarantula.getDeathsCaused() ?
            tameTarantula : tarantula;
   }
   
   @Produces public Spider getNullSpider()
   {
      return null;
   }
   
   @Produces public FunnelWeaver<Spider> getFunnelWeaverSpider()
   {
      return new FunnelWeaver<Spider>("Weaver");
   }

   @Produces public Animal makeASpider()
   {
      return new WolfSpider();
   }
   
   @Produces public int getWolfSpiderSize()
   {
      return 4;
   }
   
   @Produces public Spider[] getSpiders()
   {
      return ALL_SPIDERS;
   }
   
   @Produces @Named @RequestScoped @Tame public DaddyLongLegs produceDaddyLongLegs()
   {
      return new DaddyLongLegs();
   }

   @Produces @Named @Tame public LadybirdSpider getLadybirdSpider()
   {
      return new LadybirdSpider();
   }

   @Produces @Named("blackWidow") @Tame public BlackWidow produceBlackWidow()
   {
      return new BlackWidow();
   }
   
   @Produces @AnimalStereotype @Tame public WolfSpider produceWolfSpider()
   {
      return new WolfSpider();
   }
   
   @Produces public Bite getBite()
   {
      return new Bite() {};
   }
   
}

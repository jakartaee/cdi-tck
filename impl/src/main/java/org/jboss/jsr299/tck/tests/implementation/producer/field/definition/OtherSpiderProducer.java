package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

class OtherSpiderProducer
{
   
   public static final BlackWidow BLACK_WIDOW = new BlackWidow();
   public static final WolfSpider WOLF_SPIDER = new WolfSpider();
   
   @Produces @Pet public WolfSpider produceWolfSpider = WOLF_SPIDER;
   
   @Produces @Tame private BlackWidow produceBlackWidow = BLACK_WIDOW;
   
   private static Spider[] ALL_SPIDERS = { new Tarantula(), new LadybirdSpider(), new DaddyLongLegs() };
   @Produces public Spider[] getSpiders = ALL_SPIDERS;
   
   @Produces @Named("SpiderSize") public int getWolfSpiderSize = 4;
}

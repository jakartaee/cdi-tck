package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class SpiderProducer
{
   private static boolean tameSpiderDestroyed = false;
   private static boolean deadliestSpiderDestroyed = false;

   @Produces
   @Tame
   public Tarantula produceTameTarantula()
   {
      return new DefangedTarantula(0);
   }

   @Produces
   @Deadliest
   public Tarantula producesDeadliestTarantula(@Tame Tarantula tameTarantula, Tarantula tarantula)
   {
      return tameTarantula.getDeathsCaused() >= tarantula.getDeathsCaused() ? tameTarantula : tarantula;
   }

   public void destroyTameSpider(@Disposes @Tame Tarantula spider)
   {
      SpiderProducer.tameSpiderDestroyed = true;
   }

   public static void destroyDeadliestSpider(@Disposes @Deadliest Tarantula spider, Tarantula anotherSpider)
   {
      assert spider != anotherSpider;
      SpiderProducer.deadliestSpiderDestroyed = true;
   }

   public static boolean isTameSpiderDestroyed()
   {
      return tameSpiderDestroyed;
   }

   public static boolean isDeadliestSpiderDestroyed()
   {
      return deadliestSpiderDestroyed;
   }
}

package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   private static Tarantula tarantulaCreated;
   private static boolean tarantulaDestroyed;
   private static boolean destroyArgumentsSet;
   
   public @Produces @Pet Tarantula produceTarantula()
   {
      Tarantula tarantula = new Tarantula("Pete");
      tarantulaCreated = tarantula;
      resetTarantulaDestroyed();
      return tarantula;
   }
   
   public @Produces @Null Spider getNullSpider()
   {
      return null;
   }
   
   // FIXME this should be allowed
//   public void destroyTarantula(@Disposes Tarantula spider, @Current BeanManager beanManager)
//   {
//      tarantulaDestroyed = true;
//      if (beanManager != null)
//      {
//         destroyArgumentsSet = true;
//      }
//   }

   public static boolean isTarantulaCreated()
   {
      return tarantulaCreated != null;
   }

   public static boolean isTarantulaDestroyed()
   {
      return tarantulaDestroyed;
   }

   public static void resetTarantulaCreated()
   {
      SpiderProducer.tarantulaCreated = null;
   }

   public static void resetTarantulaDestroyed()
   {
      SpiderProducer.tarantulaDestroyed = false;
      SpiderProducer.destroyArgumentsSet = false;
   }

   public static boolean isDestroyArgumentsSet()
   {
      return destroyArgumentsSet;
   }

   public static Tarantula getTarantulaCreated()
   {
      return tarantulaCreated;
   }
   
   public static void reset()
   {
      resetTarantulaCreated();
      resetTarantulaDestroyed();
   }
}

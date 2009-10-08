package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Specializes 
class PreferredSpiderProducer extends SpiderProducer
{
   @Inject private Web web;
   private static Web injectedWeb;
   private static Tarantula tarantulaCreated;
   private static Tarantula tarantulaDestroyed;
   private static boolean destroyArgumentsSet;
   
   @Override
   @Produces @Pet @Specializes
   public Tarantula produceTarantula()
   {
      Tarantula tarantula = new Tarantula("Pete");
      tarantulaCreated = tarantula;
      resetTarantulaDestroyed();
      injectedWeb = web;
      return tarantula;
   }
   
   @Override
   public @Produces @Null Spider getNullSpider()
   {
      return null;
   }
   
   public void destroyTarantula(@Disposes @Pet Tarantula spider, BeanManager beanManager)
   {
      tarantulaDestroyed = spider;
      injectedWeb = web;
      if (beanManager != null)
      {
         destroyArgumentsSet = true;
      }
   }

   public static boolean isTarantulaCreated()
   {
      return tarantulaCreated != null;
   }

   public static boolean isTarantulaDestroyed()
   {
      return tarantulaDestroyed != null;
   }

   public static Tarantula getTarantulaDestroyed()
   {
      return tarantulaDestroyed;
   }
   
   public static void resetTarantulaCreated()
   {
      PreferredSpiderProducer.tarantulaCreated = null;
   }

   public static void resetTarantulaDestroyed()
   {
      PreferredSpiderProducer.tarantulaDestroyed = null;
      PreferredSpiderProducer.destroyArgumentsSet = false;
   }

   public static boolean isDestroyArgumentsSet()
   {
      return destroyArgumentsSet;
   }

   public static Tarantula getTarantulaCreated()
   {
      return tarantulaCreated;
   }
   
   public static Web getInjectedWeb()
   {
      return injectedWeb;
   }
   
   public static void resetInjections()
   {
      injectedWeb = null;
   }
   
   public static void reset()
   {
      resetTarantulaCreated();
      resetTarantulaDestroyed();
      resetInjections();
   }
   
}

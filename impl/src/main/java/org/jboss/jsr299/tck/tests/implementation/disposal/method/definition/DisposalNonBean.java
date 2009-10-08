package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import javax.enterprise.inject.Disposes;

class DisposalNonBean
{
   private static boolean spiderDestroyed = false;
   
   public DisposalNonBean(String someString)
   {
      
   }
   
   public void destroyDeadliest(@Disposes @Deadliest Tarantula spider)
   {
      spiderDestroyed = true;
   }

   public static boolean isSpiderDestroyed()
   {
      return spiderDestroyed;
   }

   public static void setSpiderDestroyed(boolean spiderDestroyed)
   {
      DisposalNonBean.spiderDestroyed = spiderDestroyed;
   }
}

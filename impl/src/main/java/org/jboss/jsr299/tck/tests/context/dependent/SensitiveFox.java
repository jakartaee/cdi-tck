package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;

@Dependent
@Named
@Default
public class SensitiveFox
{
   private static BeanManager beanManager;

   private boolean dependentContextActiveDuringCreate = false;
   
   private static boolean dependentContextActiveDuringEval = false;

   public SensitiveFox()
   {
      dependentContextActiveDuringCreate = beanManager.getContext(Dependent.class).isActive();
   }

   public String getName()
   {
      if (beanManager.getContext(Dependent.class).isActive())
      {
         dependentContextActiveDuringEval = true;
      }
      return "gavin";
   }

   public boolean isDependentContextActiveDuringCreate()
   {
      return dependentContextActiveDuringCreate;
   }

   public static boolean isDependentContextActiveDuringEval()
   {
      return dependentContextActiveDuringEval;
   }

   public static void setManager(BeanManager beanManager)
   {
      SensitiveFox.beanManager = beanManager;
   }

}

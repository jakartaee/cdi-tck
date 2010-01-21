package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

@Dependent
@Named
@Default
public class SensitiveFox
{
   private final BeanManager beanManager;

   private boolean dependentContextActiveDuringCreate = false;
   
   private static boolean dependentContextActiveDuringEval = false;

   @Inject
   public SensitiveFox(BeanManager beanManager)
   {
      dependentContextActiveDuringCreate = beanManager.getContext(Dependent.class).isActive();
      this.beanManager = beanManager;
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

}

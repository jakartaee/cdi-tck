package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

@Named("foxRun") @Stateful
public class FoxRun implements FoxRunLocal
{
   
   private static boolean destroyed = false;
   
   @Inject
   public FoxLocal fox;
   
   @Inject
   public FoxLocal anotherFox;
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }
   
   public static void setDestroyed(boolean destroyed)
   {
      FoxRun.destroyed = destroyed;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
   
   public FoxLocal getFox()
   {
      return fox;
   }
   
   @Remove
   public void remove()
   {
      
   }
   
}

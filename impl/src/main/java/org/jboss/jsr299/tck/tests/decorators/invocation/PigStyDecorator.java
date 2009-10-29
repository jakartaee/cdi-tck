package org.jboss.jsr299.tck.tests.decorators.invocation;

import java.io.Serializable;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
public class PigStyDecorator implements PigSty, Serializable
{
   private static boolean decoratorCalled = false;
   
   public static boolean isDecoratorCalled()
   {
      return decoratorCalled;
   }
   
   public static void reset()
   {
      decoratorCalled = false;
   }

   @Delegate
   transient PigSty pigSty;

   public void clean()
   {
      decoratorCalled = true;
      pigSty.clean();
   }

}

package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.context.CreationalContext;

class MyCreationalContext<T> implements CreationalContext<T>
{
   private static Object lastBeanPushed = null;
   private static boolean pushCalled = false;

   public void push(T incompleteInstance)
   {
      pushCalled = true;
      lastBeanPushed = incompleteInstance;
   }

   public static Object getLastBeanPushed()
   {
      return lastBeanPushed;
   }

   public static void setLastBeanPushed(Object lastBeanPushed)
   {
      MyCreationalContext.lastBeanPushed = lastBeanPushed;
   }

   public static boolean isPushCalled()
   {
      return pushCalled;
   }

   public static void setPushCalled(boolean pushCalled)
   {
      MyCreationalContext.pushCalled = pushCalled;
   }

}

package org.jboss.jsr299.tck.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;

public class MockCreationalContext<T> implements CreationalContext<T>
{
   private static List<Object> beansPushed = new ArrayList<Object>();
   private static Object lastBeanPushed = null;
   private static boolean pushCalled = false;
   private static boolean releaseCalled = false;

   public void push(T incompleteInstance)
   {
      pushCalled = true;
      lastBeanPushed = incompleteInstance;
      beansPushed.add(incompleteInstance);
   }

   public static Object getLastBeanPushed()
   {
      return lastBeanPushed;
   }
   
   public static List<Object> getBeansPushed()
   {
      return beansPushed;
   }

   public static void setLastBeanPushed(Object lastBeanPushed)
   {
      MockCreationalContext.lastBeanPushed = lastBeanPushed;
   }

   public static boolean isPushCalled()
   {
      return pushCalled;
   }

   public static void setPushCalled(boolean pushCalled)
   {
     MockCreationalContext.pushCalled = pushCalled;
   }
   
   public static boolean isReleaseCalled()
   {
      return releaseCalled;
   }
   
   public static void setReleaseCalled(boolean releaseCalled)
   {
      MockCreationalContext.releaseCalled = releaseCalled;
   }
   
   public static void reset()
   {
      lastBeanPushed = null;
      beansPushed.clear();
      pushCalled = false;
      releaseCalled = false;
   }
   
   public void release()
   {
      releaseCalled = true;
   }
   

}

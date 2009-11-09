package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.Interceptors;

@Interceptors(LifecycleCallbackInterceptor.class)
class SimpleBean
{
   private int id = 0;
   private static boolean echoCalled = false;

   @Interceptors(Interceptor1.class)
   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   @Interceptors(Interceptor2.class)
   public boolean testGetTimer()
   {
      return false;
   }

   @Interceptors(Interceptor3.class)
   public boolean testGetMethod()
   {
      return false;
   }

   @Interceptors(Interceptor4.class)
   public int add(int i, int j)
   {
      return i + j;
   }

   @Interceptors(Interceptor5.class)
   public int add2(int i, int j)
   {
      return i + j;
   }

   @Interceptors(Interceptor6.class)
   public int add3(int i, int j)
   {
      return i + j;
   }

   @Interceptors(Interceptor7.class)
   public void voidMethod()
   {
   }

   @Interceptors( { Interceptor8.class, Interceptor9.class })
   public void foo()
   {
   }

   @Interceptors(Interceptor10.class)
   public String echo(String s)
   {
      echoCalled = true;
      return s;
   }

   public static boolean isEchoCalled()
   {
      return echoCalled;
   }
}

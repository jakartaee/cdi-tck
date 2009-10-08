package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

class Tarantula extends Spider implements DeadlySpider
{
   private final String value;
   private static int numberCreated = 0;
   
   public Tarantula(String value)
   {
      this.value = value;
      numberCreated++;
   }
   
   public String getValue()
   {
      return value;
   }

   public static int getNumberCreated()
   {
      return numberCreated;
   }

   public static void setNumberCreated(int numberCreated)
   {
      Tarantula.numberCreated = numberCreated;
   }
}

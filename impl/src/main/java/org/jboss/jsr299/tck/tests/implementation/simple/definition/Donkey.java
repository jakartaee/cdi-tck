package org.jboss.jsr299.tck.tests.implementation.simple.definition;


class Donkey
{
   
   public static boolean constructedCorrectly = false;
   
   public Donkey()
   {
      constructedCorrectly = true;
   }
   
   public Donkey(String foo)
   {
      
   }

}

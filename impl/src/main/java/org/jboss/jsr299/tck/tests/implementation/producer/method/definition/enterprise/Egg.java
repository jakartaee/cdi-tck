package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

public class Egg
{
   private final Chicken mother;
   
   public Egg(Chicken mother)
   {
      this.mother = mother;
   }
   
   public Chicken getMother()
   {
      return mother;
   }
   
}
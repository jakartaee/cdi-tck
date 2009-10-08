package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

class Egg
{
   
   private final Chicken mother;

   public Egg(Chicken mother)
   {
      super();
      this.mother = mother;
   }
   
   public Chicken getMother()
   {
      return mother;
   }
   
}

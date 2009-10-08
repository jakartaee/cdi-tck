package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.enterprise;

public class Egg
{
   
   private final int size;

   public Egg(int size)
   {
      super();
      this.size = size;
   }
   
   /**
    * @return the size
    */
   public int getSize()
   {
      return size;
   }
   
}

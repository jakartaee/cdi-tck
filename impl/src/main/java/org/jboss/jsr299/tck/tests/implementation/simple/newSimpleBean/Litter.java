package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

class Litter
{
   private int quantity;
   
   public Litter(int quantity)
   {
      this.quantity = quantity;
   }

   public int getQuantity()
   {
      return quantity;
   }
}

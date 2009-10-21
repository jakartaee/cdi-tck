package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

class Yogurt
{
   private TropicalFruit fruit;
   
   public Yogurt(TropicalFruit fruit) {
      this.fruit = fruit;
   }

   public TropicalFruit getFruit()
   {
      return fruit;
   }
}

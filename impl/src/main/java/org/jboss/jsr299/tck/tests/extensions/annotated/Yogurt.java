package org.jboss.jsr299.tck.tests.extensions.annotated;

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

package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@SuppressWarnings( "unused" )
@ApplicationScoped
@Expensive
class Grocery implements Shop
{
   private Vegetables vegetables = null;
   private Fruit fruit;
   private boolean constructorWithParameterUsed = false;
   private TropicalFruit initializerFruit = null;
   private Bread bread = new Bread(true);
   private Water water = null;
   
   public Grocery()
   {
   }
   
   public Grocery(@Any TropicalFruit fruit) {
      constructorWithParameterUsed = true;
   }

   public void nonInjectAnnotatedInitializer(@Any Water water) {
      this.water = water;
   }
   
   @Inject
   public void initializer(@Any TropicalFruit fruit) {
      this.initializerFruit = fruit;
   }
   
   public String foo()
   {
      return "bar";
   }

   public boolean isVegetablesInjected()
   {
      return vegetables != null;
   }

   public Fruit getFruit()
   {
      return fruit;
   }
   
   public boolean isConstructorWithParameterUsed()
   {
      return constructorWithParameterUsed;
   }

   public TropicalFruit getInitializerFruit()
   {
      return initializerFruit;
   }

   public Milk getMilk()
   {
      return new Milk(true);
   }

   @Produces @Cheap
   public Yogurt getYogurt(@Any TropicalFruit fruit)
   {
      return new Yogurt(fruit);
   }
   
   public boolean isWaterInjected() {
      return water != null;
   }
}

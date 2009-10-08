package org.jboss.jsr299.tck.tests.extensions.producer;

import javax.inject.Inject;

class Cat
{
   private static boolean constructorCalled;
   private static boolean initializerCalled;
   
   @Inject
   protected CatFoodDish foodDish;
   
   private Bird bird;
   
   @Inject
   public Cat(LitterBox litterBox)
   {
      constructorCalled = true;
      assert litterBox != null;
   }
   
   @Inject
   public void setBird(Bird bird)
   {
      assert bird != null;
      initializerCalled = true;
   }

   public static boolean isConstructorCalled()
   {
      return constructorCalled;
   }
   
   public static boolean isInitializerCalled()
   {
      return initializerCalled;
   }

   public static void reset()
   {
      Cat.constructorCalled = false;
      Cat.initializerCalled = false;
   }
   
   public void ping()
   {
      
   }
}

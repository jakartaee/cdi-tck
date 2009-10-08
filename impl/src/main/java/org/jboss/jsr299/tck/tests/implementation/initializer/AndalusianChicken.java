package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AndalusianChicken implements LocalChicken
{
   
   public static boolean nonBusinessMethodCalled = false;

   public void firstBusinessMethod()
   {
   }

   @Inject
   public void nonBusinessMethod()
   {
      nonBusinessMethodCalled = true;
   }
   
   public void cluck()
   {
   }
}

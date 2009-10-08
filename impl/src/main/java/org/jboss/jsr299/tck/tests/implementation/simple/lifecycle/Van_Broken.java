package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

class Van_Broken
{
   
   public Van_Broken()
   {
      throw new FooException();
   }
   
}

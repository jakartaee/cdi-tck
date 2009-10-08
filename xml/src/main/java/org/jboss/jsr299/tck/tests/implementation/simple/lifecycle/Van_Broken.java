package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

class Van_Broken
{
   
   public Van_Broken() throws Exception
   {
      throw new FooException();
   }
   
}

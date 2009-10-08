package org.jboss.jsr299.tck.tests.implementation.initializer;

@Preferred
class PreferredChicken implements ChickenInterface
{

   public String getName()
   {
      return "Preferred";
   }

}

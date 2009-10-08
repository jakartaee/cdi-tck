package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.noextend3;

import javax.enterprise.inject.Specializes;

@Specializes
class Cow_Broken extends Mammal
{
   public Cow_Broken()
   {
      super("Herbivore");
   }
}

package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.broken.type.variable;

import javax.inject.Inject;

@SuppressWarnings("unused")
class Farm
{
   @Inject
   public <T extends Animal> void setAnimal(T animal)
   {
      
   }
}

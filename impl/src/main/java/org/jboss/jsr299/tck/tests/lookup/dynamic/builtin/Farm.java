package org.jboss.jsr299.tck.tests.lookup.dynamic.builtin;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

class Farm extends AbstractAnimal
{
   @Inject
   private Instance<Animal> animal;
   @Inject
   private Instance<AbstractAnimal> abstractAnimal;
   @Inject
   private Instance<Cow> cow;

   public Instance<Animal> getAnimal()
   {
      return animal;
   }

   public Instance<AbstractAnimal> getAbstractAnimal()
   {
      return abstractAnimal;
   }

   public Instance<Cow> getCow()
   {
      return cow;
   }

}

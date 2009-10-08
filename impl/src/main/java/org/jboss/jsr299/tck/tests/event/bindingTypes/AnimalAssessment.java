package org.jboss.jsr299.tck.tests.event.bindingTypes;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class AnimalAssessment
{
   @Inject @Any Event<Animal> animalEvent;
   
   @Inject @Tame Event<Animal> tameAnimalEvent;
   
   @Inject @Any @Wild Event<Animal> wildAnimalEvent;
   
   public void classifyAsTame(Animal animal)
   {
      tameAnimalEvent.fire(animal);
   }
   
   public void classifyAsWild(Animal animal)
   {
      wildAnimalEvent.fire(animal);
   }
   
   public void assess(Animal animal)
   {
      animalEvent.fire(animal);
   }
}

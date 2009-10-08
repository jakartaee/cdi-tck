package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;

class SpiderAsAnimalProducer
{
   @Produces @AsAnimal
   public Animal makeASpider = new WolfSpider();

}

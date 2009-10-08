package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;


class Chicken
{
   
   @Produces @Foo
   private Egg egg = new Egg(this);
   
}

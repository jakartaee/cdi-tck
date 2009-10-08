package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class Chicken implements ChickenLocal
{
   
   public static final int SIZE = 5;
   
   @Produces @Foo
   private static Egg egg = new Egg(SIZE);  
}

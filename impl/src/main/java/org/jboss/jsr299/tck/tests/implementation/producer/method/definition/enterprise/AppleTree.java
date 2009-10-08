package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class AppleTree implements AppleTreeLocal
{
   @Produces @Yummy public Apple produceApple()
   {
      return new Apple(this);
   }
}

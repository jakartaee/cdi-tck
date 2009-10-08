package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.inject.Produces;

class AppleTree
{
   @Produces @Yummy public Apple produceApple()
   {
      return new Apple(this);
   }
}

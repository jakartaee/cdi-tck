package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class PearTree implements PearTreeLocal
{
   @Produces @Yummy public Pear produceLightYellowPear()
   {
      return new Pear("green");
   }
}

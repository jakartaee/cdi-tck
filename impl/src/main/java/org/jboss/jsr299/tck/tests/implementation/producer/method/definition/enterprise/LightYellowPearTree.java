package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class LightYellowPearTree extends YellowPearTree implements LightYellowPearTreeLocal
{
   @Produces @Yummy @LightYellow public Pear produceLightYellowPear()
   {
      return new Pear("lightYellow");
   }
}

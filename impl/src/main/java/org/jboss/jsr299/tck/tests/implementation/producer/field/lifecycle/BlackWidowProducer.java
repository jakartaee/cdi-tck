package org.jboss.jsr299.tck.tests.implementation.producer.field.lifecycle;

import javax.enterprise.inject.Produces;

public class BlackWidowProducer
{
   public static BlackWidow blackWidow = new BlackWidow();
   @Produces @Tame
   public BlackWidow produceBlackWidow = blackWidow;

}

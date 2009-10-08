package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class BlackWidowProducer
{
   public static BlackWidow blackWidow = new BlackWidow();
   @Produces
   @Named("blackWidow")
   public BlackWidow produceBlackWidow = blackWidow;

}

package org.jboss.jsr299.tck.tests.implementation.producer.field.lifecycle;

import javax.enterprise.inject.Produces;

public class TarantulaProducer
{
   @Produces @Tame public Tarantula produceTarantula = new DefangedTarantula();

}

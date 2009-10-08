package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.inject.Produces;

class TarantulaProducer
{
   @Produces @Pet public Tarantula produceTarantula = new Tarantula();

}

package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

class TameTarantulaProducer
{
   @Produces @Foo @Tame @RequestScoped public Tarantula produceTarantula = new DefangedTarantula();

}

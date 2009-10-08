package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

class CityProducer
{
   @Produces public Violation reference = new Violation();
   
   @Produces @SessionScoped public HelsinkiNonSerializable helsinki = new HelsinkiNonSerializable();
}

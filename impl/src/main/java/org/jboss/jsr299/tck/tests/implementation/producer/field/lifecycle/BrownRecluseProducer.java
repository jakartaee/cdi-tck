package org.jboss.jsr299.tck.tests.implementation.producer.field.lifecycle;

import javax.enterprise.inject.Produces;

class BrownRecluseProducer
{
   @Produces
   protected BrownRecluse spider = new BrownRecluse(5);
}

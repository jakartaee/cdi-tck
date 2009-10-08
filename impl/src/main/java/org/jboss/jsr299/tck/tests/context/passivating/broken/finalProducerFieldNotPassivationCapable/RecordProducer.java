package org.jboss.jsr299.tck.tests.context.passivating.broken.finalProducerFieldNotPassivationCapable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

class RecordProducer
{
   public @RequestScoped @Produces Broken_Record record = new Broken_Record();
}

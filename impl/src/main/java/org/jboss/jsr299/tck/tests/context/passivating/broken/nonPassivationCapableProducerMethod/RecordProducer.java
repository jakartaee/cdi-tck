package org.jboss.jsr299.tck.tests.context.passivating.broken.nonPassivationCapableProducerMethod;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

class RecordProducer
{
   public final @Produces @SessionScoped Broken_Record getRecord()
   {
      return new Broken_Record();
   }
}

package org.jboss.jsr299.tck.tests.context.passivating.broken.nonPassivationCapableProducerField;

import javax.enterprise.inject.Produces;

class RecordProducer
{
   public @Produces @FooScoped Broken_Record record = new Broken_Record();
}

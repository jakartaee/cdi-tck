package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

public class RecordProducer
{
   public @Produces @SessionScoped Record getRecord()
   {
      return new Record();
   }
}

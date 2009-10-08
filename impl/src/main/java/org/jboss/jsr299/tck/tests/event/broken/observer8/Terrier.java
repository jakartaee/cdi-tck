package org.jboss.jsr299.tck.tests.event.broken.observer8;

import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;

import javax.enterprise.event.Observes;

class Terrier
{
   public void observer(@Observes(during=BEFORE_COMPLETION) String event)
   {
   }
}

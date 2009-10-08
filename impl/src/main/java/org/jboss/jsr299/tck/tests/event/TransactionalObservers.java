package org.jboss.jsr299.tck.tests.event;

import static javax.enterprise.event.TransactionPhase.AFTER_COMPLETION;
import static javax.enterprise.event.TransactionPhase.AFTER_FAILURE;
import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;
import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;

import javax.enterprise.event.Observes;

class TransactionalObservers
{
   public void train(@Observes(during=BEFORE_COMPLETION) DisobedientDog dog)
   {
   }

   public void trainNewTricks(@Observes(during=AFTER_COMPLETION) ShowDog dog)
   {
   }

   public void trainCompanion(@Observes(during=AFTER_FAILURE) SmallDog dog)
   {
   }

   public void trainSightSeeing(@Observes(during=AFTER_SUCCESS) LargeDog dog)
   {
   }

}

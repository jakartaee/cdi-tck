package org.jboss.jsr299.tck.tests.lookup.dynamic;

import static org.jboss.jsr299.tck.tests.lookup.dynamic.PayBy.PaymentMethod.CHEQUE;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

class ObtainsInstanceBean
{
   @Inject @PayBy(CHEQUE) Instance<AsynchronousPaymentProcessor> paymentProcessor;
   @Inject @Any Instance<PaymentProcessor> anyPaymentProcessor;
   
   public Instance<AsynchronousPaymentProcessor> getPaymentProcessor()
   {
      return paymentProcessor;
   }

   public Instance<PaymentProcessor> getAnyPaymentProcessor()
   {
      return anyPaymentProcessor;
   }
}

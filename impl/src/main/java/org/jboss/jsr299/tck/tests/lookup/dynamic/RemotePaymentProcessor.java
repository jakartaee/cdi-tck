package org.jboss.jsr299.tck.tests.lookup.dynamic;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.jsr299.tck.tests.lookup.dynamic.PayBy.PaymentMethod;

@ApplicationScoped
@PayBy(PaymentMethod.CREDIT_CARD)
class RemotePaymentProcessor implements AsynchronousPaymentProcessor
{
   private int value = 0;

   public int getValue()
   {
      return value;
   }

   public void setValue(int value)
   {
      this.value = value;
   }
}

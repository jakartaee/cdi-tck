package org.jboss.jsr299.tck.tests.lookup.dynamic;

interface AsynchronousPaymentProcessor extends PaymentProcessor
{
   int getValue();
   void setValue(int value);
}

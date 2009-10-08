package org.jboss.jsr299.tck.tests.lookup.dynamic;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.jsr299.tck.tests.lookup.dynamic.PayBy.PaymentMethod;

@PayBy(PaymentMethod.CASH)
@ApplicationScoped
class SimplePaymentProcessor implements SynchronousPaymentProcessor
{
}

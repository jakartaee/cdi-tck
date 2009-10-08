package org.jboss.jsr299.tck.tests.definition.qualifier;

import javax.inject.Inject;

class Order
{
   @Inject
   public Order(OrderProcessor processor) {} 
}

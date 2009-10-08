package org.jboss.jsr299.tck.tests.lookup.injection.any;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

class Customer
{
   @Inject @Any Drink drink;
}

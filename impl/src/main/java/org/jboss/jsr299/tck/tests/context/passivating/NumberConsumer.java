package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
class NumberConsumer
{
   
   @Inject int number;
   
   public void ping(){};
   
}

package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

public class WheatProducer
{
   public @Produces @SessionScoped Wheat wheat = new Wheat();
}

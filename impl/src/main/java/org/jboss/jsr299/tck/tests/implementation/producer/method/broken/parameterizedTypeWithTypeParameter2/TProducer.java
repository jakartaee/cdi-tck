package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterizedTypeWithTypeParameter2;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class TProducer
{
   @Produces
   public <T> T create(InjectionPoint point)
   {
       return null;
   } 
}


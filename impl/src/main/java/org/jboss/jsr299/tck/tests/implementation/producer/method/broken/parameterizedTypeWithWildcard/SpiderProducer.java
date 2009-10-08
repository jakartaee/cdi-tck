package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterizedTypeWithWildcard;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   
   @Produces public FunnelWeaver<?> getFunnelWeaver()
   {
      return new FunnelWeaver<Object>();
   }
   
}

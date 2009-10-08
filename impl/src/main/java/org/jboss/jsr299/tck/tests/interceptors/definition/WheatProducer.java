package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.enterprise.inject.Produces;
import javax.interceptor.Interceptors;

@Interceptors(MissileInterceptor.class)
class WheatProducer
{
   @Produces Wheat createWheat() 
   {
      return new Wheat(null);
   }
}

package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerMethodReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import javax.enterprise.inject.Produces;

public class CowProducer
{
   
   @Produces @British
   public Cow produce()
   {
      return new Cow();
   }

}

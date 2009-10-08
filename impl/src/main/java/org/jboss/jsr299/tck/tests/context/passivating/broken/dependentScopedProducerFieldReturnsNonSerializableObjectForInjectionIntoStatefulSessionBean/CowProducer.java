package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerFieldReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import javax.enterprise.inject.Produces;

public class CowProducer
{   
   @Produces @British public Cow cow = new Cow();
}

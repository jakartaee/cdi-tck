package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerMethodReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;

@Stateful
public class Maarianhamina_Broken implements MaarianHaminaLocal_Broken
{
   @SuppressWarnings("unused")
   @Inject @British Cow cow;
   
   @Remove
   public void bye() {
   }
}

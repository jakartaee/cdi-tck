package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonSerializableIntializerMethod;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@Stateful
@SessionScoped
class Espoo_Broken implements EspooLocal_Broken
{
   @Inject
   District district;
   
   @Remove
   public void bye() {
   }
}

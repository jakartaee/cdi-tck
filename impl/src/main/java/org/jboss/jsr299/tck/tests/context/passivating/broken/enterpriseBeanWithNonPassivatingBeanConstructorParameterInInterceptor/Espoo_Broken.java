package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingBeanConstructorParameterInInterceptor;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@Stateful
@SessionScoped
@CityBinding
class Espoo_Broken implements EspooLocal_Broken
{  
   @Remove
   public void bye() {
   }
}

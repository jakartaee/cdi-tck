package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInitializerInDecorator;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.interceptor.Interceptors;

@Stateful
@SessionScoped
@Interceptors(BrokenDecorator.class)
class Espoo_Broken implements EspooLocal_Broken
{  
   @Remove
   public void bye() {
   }
}

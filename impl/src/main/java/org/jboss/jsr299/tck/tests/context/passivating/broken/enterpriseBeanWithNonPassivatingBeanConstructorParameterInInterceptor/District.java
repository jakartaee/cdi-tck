package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingBeanConstructorParameterInInterceptor;

import javax.enterprise.context.Dependent;

@Dependent
class District
{   
   public void ping()
   {
      
   }

}

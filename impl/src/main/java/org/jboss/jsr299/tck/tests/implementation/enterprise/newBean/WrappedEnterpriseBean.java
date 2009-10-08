package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Stateful
@Named("John")
class WrappedEnterpriseBean implements WrappedEnterpriseBeanLocal
{
   @Remove
   public void bye() 
   {
   }
}

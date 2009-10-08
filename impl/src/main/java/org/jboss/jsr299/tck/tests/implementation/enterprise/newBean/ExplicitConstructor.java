package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Local;

@Local
public interface ExplicitConstructor
{
   public int getConstructorCalls();
   public void setConstructorCalls(int numCalls);
   
   public SimpleBean getInjectedSimpleBean();
   public void setInjectedSimpleBean(SimpleBean injectedSimpleBean);

}

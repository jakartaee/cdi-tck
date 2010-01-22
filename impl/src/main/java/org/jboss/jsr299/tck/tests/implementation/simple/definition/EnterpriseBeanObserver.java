package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;

public class EnterpriseBeanObserver implements Extension
{
   
   public static boolean observedEnterpriseBean;
   public static boolean observedAnotherBean;
   
   public void observeAnotherBean(@Observes ProcessManagedBean<Sheep> event)
   {
      observedAnotherBean = true;
   }
   
   public void observeEnterpriseBean(@Observes ProcessManagedBean<MockEnterpriseBean> event)
   {
      observedEnterpriseBean = true;
   }

}

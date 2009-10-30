package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class AfterBeanDiscoveryObserver implements Extension
{
   private static MyContextual bean = null;
   
   public void addCustomBeanImplementation(@Observes AfterBeanDiscovery event, BeanManager manager) {
      bean = new MyContextual(manager);
      event.addBean(bean);
   }
   
   public void addNewContexts(@Observes AfterBeanDiscovery event)
   {
      event.addContext(new DummyContext());
      event.addContext(new DummyContext());
   }

   public static MyContextual getBean()
   {
      return bean;
   }
}

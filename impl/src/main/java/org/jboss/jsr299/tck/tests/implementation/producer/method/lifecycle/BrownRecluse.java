package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;

class BrownRecluse
{
   public @Produces @FirstBorn SpiderEgg layAnEgg(BeanManager beanManager)
   {
      assert beanManager != null : "Manager was not injected";
      return new SpiderEgg();
   }
}

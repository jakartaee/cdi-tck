package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Stateful
public class DependentSessionBean implements DependentSessionInterface
{
   @Inject
   private StateKeeper stateKeeper;

   @Remove
   public void remove()
   {
      stateKeeper.setRemoveCalled(true);
   }

   @Remove
   public void anotherRemoveWithParameters(String reason, @Default BeanManager manager)
   {
      stateKeeper.setRemoveCalled(true);
      assert manager == null;
   }

   @PreDestroy
   public void markDestroyed()
   {
      stateKeeper.setBeanDestroyed(true);
   }

   public void businessMethod()
   {
   }
}

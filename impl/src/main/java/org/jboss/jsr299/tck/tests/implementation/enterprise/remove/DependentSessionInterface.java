package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import javax.ejb.Local;
import javax.enterprise.inject.spi.BeanManager;

@Local
public interface DependentSessionInterface
{
   public void remove();
   
   public void anotherRemoveWithParameters(String reason, BeanManager manager);
   
   public void businessMethod();
}

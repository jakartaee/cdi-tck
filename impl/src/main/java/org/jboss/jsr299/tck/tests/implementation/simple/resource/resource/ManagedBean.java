package org.jboss.jsr299.tck.tests.implementation.simple.resource.resource;

import java.io.Serializable;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

class ManagedBean implements Serializable
{
   @Inject @Another
   private BeanManager beanManager;

   public BeanManager getBeanManager()
   {
      return beanManager;
   }
}

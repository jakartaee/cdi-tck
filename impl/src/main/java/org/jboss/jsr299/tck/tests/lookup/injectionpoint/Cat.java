package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

class Cat
{
   @Inject private InjectionPoint injectionPoint;
   
   @Inject private BeanManager beanManager;
   
   public String hello() {
      return "hello";
   }
   
   public InjectionPoint getInjectionPoint() {
      return injectionPoint;
   }

   public BeanManager getBeanManager()
   {
      return beanManager;
   }
}

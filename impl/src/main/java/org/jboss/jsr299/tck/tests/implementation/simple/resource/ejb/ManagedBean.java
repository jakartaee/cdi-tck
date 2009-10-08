package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import java.io.Serializable;

import javax.inject.Inject;


class ManagedBean implements Serializable
{
   @Inject @Lazy 
   private BeanRemote myEjb;
   
   public BeanRemote getMyEjb()
   {
      return myEjb;
   }

}

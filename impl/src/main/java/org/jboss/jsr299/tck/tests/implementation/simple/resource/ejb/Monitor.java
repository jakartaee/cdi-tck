package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

@RequestScoped 
class Monitor implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 4495864336368063408L;
	private boolean remoteEjbDestroyed = false;
   
   public void remoteEjbDestroyed()
   {
      remoteEjbDestroyed = true;
   }
   
   public boolean isRemoteEjbDestroyed()
   {
      return remoteEjbDestroyed;
   }
}

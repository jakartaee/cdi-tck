package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class StateKeeper implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = -7168331316716402245L;
private boolean removeCalled  = false;
   private boolean beanDestroyed = false;

   public boolean isRemoveCalled()
   {
      return removeCalled;
   }

   public void setRemoveCalled(boolean removeCalled)
   {
      this.removeCalled = removeCalled;
   }

   public boolean isBeanDestroyed()
   {
      return beanDestroyed;
   }

   public void setBeanDestroyed(boolean beanDestroyed)
   {
      this.beanDestroyed = beanDestroyed;
   }

}

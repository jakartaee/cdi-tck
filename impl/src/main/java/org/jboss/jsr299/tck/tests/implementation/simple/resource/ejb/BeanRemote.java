package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import javax.ejb.Remote;

@Remote
public interface BeanRemote extends AnotherInterface
{
   public String knockKnock();
   
   public int getKnocks();
   
   public boolean isUserTransactionInjected();
   
   public void cleanup();
   
   public void dispose();
}

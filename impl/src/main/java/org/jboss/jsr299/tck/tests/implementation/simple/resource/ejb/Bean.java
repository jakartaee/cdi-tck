package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class Bean implements BeanRemote
{
   private int knocks = 0;
   
   private @Inject Monitor monitor;
   
   private @Resource UserTransaction transaction;
   
   public String knockKnock()
   {
      knocks++;
      return "We're home";
   }
   
   public int getKnocks()
   {
      return knocks;
   }
   
   public boolean isUserTransactionInjected()
   {
      try
      {
         if (transaction != null)
         {
            transaction.getStatus();
            return true;
         }
      }
      catch (SystemException e)
      {
      }
      return false;
   }

   @PreDestroy 
   public void cleanup()
   {
      monitor.remoteEjbDestroyed();
   }

   @Remove
   public void dispose()
   {
   }
}

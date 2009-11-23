package org.jboss.jsr299.tck.tests.event.observer.transactional;

import static javax.ejb.TransactionManagementType.BEAN;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;

@Stateless
@TransactionManagement(BEAN)
@Named
@Default
public class DogAgent implements Agent
{
   @Resource
   private UserTransaction userTransaction;

   @Inject
   private BeanManager jsr299Manager;

   public void sendInTransaction(Object event)
   {
      try
      {
         userTransaction.begin();
         jsr299Manager.fireEvent(event);
         userTransaction.commit();
      }
      catch (EJBException ejbException)
      {
         throw ejbException;
      }
      catch (Exception e)
      {
         throw new EJBException("Transaction failure", e);
      }
   }
   
   public void sendInTransactionAndFail(Object event) throws Exception
   {
      try
      {
         userTransaction.begin();
         jsr299Manager.fireEvent(event);
         throw new FooException();
      }
      finally
      {
         userTransaction.rollback();
      }
      
   }

   public void sendOutsideTransaction(Object event)
   {
      jsr299Manager.fireEvent(event);
   }
}

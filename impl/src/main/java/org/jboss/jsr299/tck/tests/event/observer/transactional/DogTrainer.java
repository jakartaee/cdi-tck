package org.jboss.jsr299.tck.tests.event.observer.transactional;

import static javax.ejb.TransactionManagementType.BEAN;
import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Observes;
import javax.transaction.UserTransaction;

@Stateless
@TransactionManagement(BEAN)
public class DogTrainer implements Trainer
{
   @Resource
   private UserTransaction userTransaction;

   public void train(@Observes(during=BEFORE_COMPLETION) DisobedientDog dog)
   {
      try
      {
         userTransaction.setRollbackOnly();
      }
      catch (Exception e)
      {
         throw new EJBException("Failed to set transaction rollback only", e);
      }
   }

   public void trainNewTricks(@Observes(during=BEFORE_COMPLETION) ShowDog dog)
   {
      try
      {
         userTransaction.begin();
         userTransaction.rollback();
      }
      catch (Exception e)
      {
         throw new EJBException("Failed to start new transaction", e);
      }
   }

   public void trainCompanion(@Observes(during=BEFORE_COMPLETION) SmallDog dog)
   {
      try
      {
         userTransaction.rollback();
      }
      catch (Exception e)
      {
         throw new EJBException("Failed to start new transaction", e);
      }
   }

   public void trainSightSeeing(@Observes(during=BEFORE_COMPLETION) LargeDog dog)
   {
      try
      {
         userTransaction.commit();
      }
      catch (Exception e)
      {
         throw new EJBException("Failed to start new transaction", e);
      }
   }

}

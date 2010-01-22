/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

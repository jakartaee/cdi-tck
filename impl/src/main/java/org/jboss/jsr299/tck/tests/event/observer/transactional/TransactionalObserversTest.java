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

import java.math.BigInteger;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * Integration tests for Web Bean events.
 * 
 * @author David Allen
 * 
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class TransactionalObserversTest extends AbstractJSR299Test
{
   private PomeranianInterface dog = null;
   
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      dog = (PomeranianInterface) getInstanceByName("Teddy");
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertion(section = "10.4.4", id = "a")
   public void testTransactionalObserverNotifiedImmediatelyWhenNoTransactionInProgress()
   {
      dog.setCorrectContext(false);
      dog.setCorrectTransactionState(false);
      Agent dogAgent = getInstanceByType(Agent.class);
      assert dogAgent != null;
      dogAgent.sendOutsideTransaction(BigInteger.TEN);
      assert dog.isCorrectTransactionState();
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertion(section = "10.4.4", id = "c")
   public void testAfterTransactionCompletionObserver() throws InterruptedException
   {
      dog.setCorrectContext(false);
      dog.setCorrectTransactionState(false);
      assert !getCurrentManager().resolveObserverMethods("event").isEmpty();
      Agent dogAgent = getInstanceByType(Agent.class);
      dogAgent.sendInTransaction("event");
      Thread.sleep(100);
      assert dog.isCorrectTransactionState();
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertion(section = "10.4.4", id = "d")
   public void testAfterTransactionSuccessObserver() throws InterruptedException
   {
      dog.setCorrectContext(false);
      dog.setCorrectTransactionState(false);
      Agent dogAgent = getInstanceByType(Agent.class);
      dogAgent.sendInTransaction(new Integer(4));
      Thread.sleep(100);
      assert dog.isCorrectTransactionState();
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertion(section = "10.4.4", id = "e")
   public void testAfterTransactionFailureObserver() throws Exception
   {
      dog.setCorrectContext(false);
      dog.setCorrectTransactionState(false);
      Agent dogAgent = getInstanceByType(Agent.class);
      try
      {
         dogAgent.sendInTransactionAndFail(new Float(4.0));
      }
      catch (Exception e)
      {
         if (!isThrowablePresent(FooException.class, e))
         {
            throw e;
         }
      }
      Thread.sleep(100);
      assert dog.isCorrectTransactionState();
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "10.4.4", id = "b"),
      @SpecAssertion(section = "10.4.4", id = "e") })
   public void testBeforeTransactionCompletionObserver()
   {
      dog.setCorrectContext(false);
      dog.setCorrectTransactionState(false);
      Agent dogAgent = getInstanceByType(Agent.class);
      dogAgent.sendInTransaction(new RuntimeException("test event"));
      assert dog.isCorrectTransactionState();
   }

   @Test(groups = { "events", "integration" })
   @SpecAssertion(section = "10.5", id = "bd")
   public void testObserverCanSetRollbackOnlyOnTransaction()
   {
      Agent dogAgent = getInstanceByType(Agent.class);
      dogAgent.sendInTransaction(new DisobedientDog());
   }
}

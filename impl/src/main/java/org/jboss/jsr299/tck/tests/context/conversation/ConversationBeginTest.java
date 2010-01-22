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
package org.jboss.jsr299.tck.tests.context.conversation;

import javax.enterprise.context.Conversation;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * @author Nicklas Karlsson
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ConversationBeginTest extends AbstractJSR299Test
{
   
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      Conversation conversation = getInstanceByType(Conversation.class, new AnyLiteral());
      if (!conversation.isTransient())
      {
         conversation.end();
      }
   }

   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "f")
   public void testConversationBeginMakesConversationLongRunning()
   {
      Conversation conversation = getInstanceByType(Conversation.class, new AnyLiteral());
      assert conversation.isTransient();
      conversation.begin();
      assert !conversation.isTransient();
   }

   @Test(groups = { "contexts" }, expectedExceptions = IllegalStateException.class)
   @SpecAssertion(section = "6.7.5", id = "r")
   public void testBeginAlreadyLongRunningConversationThrowsException()
   {
      Conversation conversation = getInstanceByType(Conversation.class, new AnyLiteral());
      assert conversation.isTransient();
      conversation.begin();
      conversation.begin();
   }

}
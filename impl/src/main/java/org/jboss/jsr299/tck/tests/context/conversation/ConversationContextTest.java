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

import java.lang.annotation.Annotation;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;

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
public class ConversationContextTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "e")
   public void testDefaultConversationIsTransient()
   {
      assert getInstanceByType(Conversation.class, new AnyLiteral()).isTransient();
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "iaa")
   public void testBeanWithTypeConversation()
   {
      assert getBeans(Conversation.class, new AnyLiteral()).size() == 1;
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "ib")
   public void testBeanWithRequestScope()
   {
      assert getBeans(Conversation.class, new AnyLiteral()).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "id")
   public void testBeanWithBindingCurrent()
   {
      boolean found = false;
      for (Annotation binding : getBeans(Conversation.class, new AnyLiteral()).iterator().next().getQualifiers())
      {
         if (binding.annotationType().equals(Default.class))
         {
            found = true;
         }
      }
      assert found;
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "l")
   public void testTransientConversationHasNullId()
   {
      Conversation conversation = getInstanceByType(Conversation.class, new AnyLiteral());
      assert conversation.isTransient();
      assert conversation.getId() == null;
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "ie")
   public void testBeanWithNameJavaxEnterpriseContextConversation()
   {
      assert getBeans(Conversation.class, new AnyLiteral()).iterator().next().getName().equals("javax.enterprise.context.conversation");
   }

}
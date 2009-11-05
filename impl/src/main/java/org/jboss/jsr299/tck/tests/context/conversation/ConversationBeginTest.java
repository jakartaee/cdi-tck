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
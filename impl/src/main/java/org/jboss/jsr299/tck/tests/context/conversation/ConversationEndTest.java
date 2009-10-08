package org.jboss.jsr299.tck.tests.context.conversation;

import javax.enterprise.context.Conversation;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * @author Nicklas Karlsson
 */
@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ConversationEndTest extends AbstractJSR299Test
{
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      Conversation conversation = getInstanceByType(Conversation.class);
      if (conversation.isLongRunning())
      {
         conversation.end();
      }
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "g"),
      @SpecAssertion(section = "6.7.5", id = "k"),
      @SpecAssertion(section = "6.7.5", id = "o")
   })
   public void testConversationEndMakesConversationTransient()
   {
      Conversation conversation = getInstanceByType(Conversation.class);
      assert conversation.isTransient();
      conversation.begin();
      assert conversation.isLongRunning();
      assert !conversation.isTransient();
      conversation.end();
      assert !conversation.isLongRunning();
      assert conversation.isTransient();
   }
   
   @Test(groups = { "contexts" }, expectedExceptions = IllegalStateException.class)
   @SpecAssertion(section = "6.7.5", id = "q")
   public void testEndTransientConversationThrowsException()
   {
      Conversation conversation = getInstanceByType(Conversation.class);
      assert conversation.isTransient();
      conversation.end();
   }

}
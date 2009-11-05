package org.jboss.jsr299.tck.tests.context.conversation;

import javax.enterprise.context.Conversation;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * @author Dan Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ConversationTimeoutTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "6.7.5", id = "m")
   public void testConversationHasDefaultTimeout()
   {
      Conversation conversation = getInstanceByType(Conversation.class);
      assert conversation.getTimeout() > 0;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "m"),
      @SpecAssertion(section = "6.7.5", id = "n")
   })
   public void testSetConversationTimeoutOverride()
   {
      Conversation conversation = getInstanceByType(Conversation.class);
      long oldValue = conversation.getTimeout();
      conversation.setTimeout(1500);
      assert conversation.getTimeout() == 1500;
      conversation.setTimeout(oldValue);
   }
}

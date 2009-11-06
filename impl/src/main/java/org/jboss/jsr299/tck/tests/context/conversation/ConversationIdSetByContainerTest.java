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
 * 
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ConversationIdSetByContainerTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "hb"),
      @SpecAssertion(section = "6.7.5", id = "j")
   })
   public void testConversationBeginMakesConversationLongRunning()
   {
      Conversation conversation = getInstanceByType(Conversation.class);
      conversation.begin();
      assert conversation.getId() != null;
   }

}
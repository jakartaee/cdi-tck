package org.jboss.jsr299.tck.tests.context.conversation.client;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.inject.Named;

public @Named class CloudController
{
   @Inject Conversation conversation;
   
   public String getBeginConversation()
   {
      conversation.begin();
      return "long-running conversation begun";
   }
   
}

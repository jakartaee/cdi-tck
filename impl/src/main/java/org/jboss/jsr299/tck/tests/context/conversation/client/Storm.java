package org.jboss.jsr299.tck.tests.context.conversation.client;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ConversationScoped
public class Storm implements Serializable
{
   
   /**
	 * 
	 */
	private static final long serialVersionUID = -1513633490356967202L;

	@Inject Conversation conversation;
   
   private String strength;
   
   public String thunder()
   {
      return "thunder";
   }
   
   public String lightening()
   {
      return "lightening";
   }
   
   public void beginConversation()
   {
      conversation.begin();
   }
   
   public String getStrength()
   {
      return strength;
   }
   
   public void setStrength(String strength)
   {
      this.strength = strength;
   }
   
}

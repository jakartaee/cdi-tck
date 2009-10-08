package org.jboss.jsr299.tck.tests.context.conversation.client;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@Named @Default
@ConversationScoped
public class Cloud implements Serializable
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 5765109971012677278L;

	public static final String NAME = Cloud.class.getName() + ".Pete";
   
   public static final String RAINED_HEADER_NAME = Cloud.class.getName() + ".rained";
   
   private static boolean destroyed = false;

   private boolean rained;
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
   
   public static void setDestroyed(boolean destroyed)
   {
      Cloud.destroyed = destroyed;
   }
   
   public String getName()
   {
      return NAME;
   }
   
   public void rain()
   {
      rained = true;
      System.out.println("rain!");
   }
   
   public boolean isRained()
   {
      return rained;
   }
   
}

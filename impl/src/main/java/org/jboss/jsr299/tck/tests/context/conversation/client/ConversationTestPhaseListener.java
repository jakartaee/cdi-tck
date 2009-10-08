package org.jboss.jsr299.tck.tests.context.conversation.client;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import org.jboss.jsr299.tck.impl.JSR299ConfigurationImpl;
import org.jboss.jsr299.tck.impl.OldSPIBridge;

public class ConversationTestPhaseListener implements PhaseListener
{
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 1197355854770726526L;

public static final String ACTIVE_BEFORE_APPLY_REQUEST_VALUES_HEADER_NAME = "org.jboss.jsr299.tck.activeBeforeApplyRequestValues";

   private boolean activeBeforeApplyRequestValues;
   
   public void afterPhase(PhaseEvent event)
   {
   }

   public void beforePhase(PhaseEvent event)
   {
      try
      {
         JSR299ConfigurationImpl.get();
      }
      catch (NoClassDefFoundError e) 
      {
         e.printStackTrace();
         e.getCause().printStackTrace();
      }
      if (event.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES))
      {
         try
         {
            JSR299ConfigurationImpl.get().getManagers().getManager().getContext(ConversationScoped.class);
            activeBeforeApplyRequestValues = true;
         }
         catch (ContextNotActiveException e)
         {
            activeBeforeApplyRequestValues = false;
         }
      }
      if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE))
      {
         BeanManager beanManager = JSR299ConfigurationImpl.get().getManagers().getManager();
         Conversation conversation = OldSPIBridge.getInstanceByType(beanManager, Conversation.class);
         HttpServletResponse response = (HttpServletResponse) event.getFacesContext().getExternalContext().getResponse();
         response.addHeader(AbstractConversationTest.CID_HEADER_NAME, conversation.getId());
         response.addHeader(AbstractConversationTest.LONG_RUNNING_HEADER_NAME, String.valueOf(conversation.isLongRunning()));
         response.addHeader(Cloud.RAINED_HEADER_NAME, new Boolean(OldSPIBridge.getInstanceByType(beanManager, Cloud.class).isRained()).toString());
         response.addHeader(ACTIVE_BEFORE_APPLY_REQUEST_VALUES_HEADER_NAME, new Boolean(activeBeforeApplyRequestValues).toString());
      }
   }

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }
   
}

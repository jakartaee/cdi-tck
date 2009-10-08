package org.jboss.jsr299.tck.tests.context.conversation.client;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.enterprise.context.Conversation;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.jsr299.tck.impl.OldSPIBridge;

public class ConversationStatusServlet extends HttpServlet
{

	private static final long serialVersionUID = 2984756941080790899L;
	
	@Inject
   private BeanManager beanManager;
   
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      String method = req.getParameter("method");
      if ("cid".equals(method))
      {
         Conversation conversation = OldSPIBridge.getInstanceByType(beanManager, Conversation.class);
         serializeToResponse(conversation.getId(), resp);
      }
      else if ("cloudDestroyed".equals(method))
      {
         if (Cloud.isDestroyed())
         {
            resp.setStatus(HttpServletResponse.SC_OK);
         }
         else
         {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         }
      }
      else if ("invalidateSession".equals(method))
      {
         req.getSession().invalidate();
      }
      else if ("resetCloud".equals(method))
      {
         Cloud.setDestroyed(false);
      }
      else
      {
         resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
   }
   
   private void serializeToResponse(Object object, HttpServletResponse resp) throws IOException
   {
      ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
      oos.writeObject(object);
      oos.flush();
      oos.close();
   }
   
}

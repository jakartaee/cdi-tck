package org.jboss.jsr299.tck.tests.context.session;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvalidateSession extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   @Inject
   private BeanManager jsr299Manager;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      resp.setContentType("text/text");

      if (req.getParameter("timeout") != null)
      {
         SimpleSessionBean.setBeanDestroyed(false);
         org.jboss.jsr299.tck.impl.OldSPIBridge.getInstanceByType(jsr299Manager,SimpleSessionBean.class);
         req.getSession().setMaxInactiveInterval(Integer.parseInt(req.getParameter("timeout")));
      }
      else if (req.getParameter("isBeanDestroyed") != null)
      {
         resp.getWriter().print(SimpleSessionBean.isBeanDestroyed());
      }
      else
      {
         SimpleSessionBean.setBeanDestroyed(false);
         org.jboss.jsr299.tck.impl.OldSPIBridge.getInstanceByType(jsr299Manager,SimpleSessionBean.class);
         req.getSession().invalidate();
      }
   }
}

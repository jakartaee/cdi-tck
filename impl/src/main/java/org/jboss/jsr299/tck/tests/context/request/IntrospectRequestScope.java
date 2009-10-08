package org.jboss.jsr299.tck.tests.context.request;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IntrospectRequestScope extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   @Inject
   private BeanManager jsr299Manager;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      resp.setContentType("text/text");
      SimpleRequestBean aBean = org.jboss.jsr299.tck.impl.OldSPIBridge.getInstanceByType(jsr299Manager,SimpleRequestBean.class);
      resp.getWriter().print(aBean.getId());
   }

}

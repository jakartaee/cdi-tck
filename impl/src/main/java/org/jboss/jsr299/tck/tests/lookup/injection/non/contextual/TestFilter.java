package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class TestFilter implements Filter
{

   @Inject
   private Sheep sheep;
   private boolean injectionPerformedCorrectly = false;
   private boolean initializerCalled = false;
   private boolean initCalledAfterInitializer = false;

   @Inject
   public void initialize(Sheep sheep) {
      initializerCalled = sheep != null;
   }
   
   public void destroy()
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      HttpServletResponse resp = (HttpServletResponse) response;
      
      if (request.getParameter("test").equals("injection")) {
         // Return 200 if injection into Filter occurred, 500 otherwise
         resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
      } else if (request.getParameter("test").equals("initializer")) {
         // Return 200 if initializer was called, 500 otherwise
         resp.setStatus(initCalledAfterInitializer ? 200 : 500);
      } else {
         resp.setStatus(404);
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
      injectionPerformedCorrectly = sheep != null;
      initCalledAfterInitializer = initializerCalled;
   }
}

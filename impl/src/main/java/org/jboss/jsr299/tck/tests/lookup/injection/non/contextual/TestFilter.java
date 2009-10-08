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

   public void destroy()
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      HttpServletResponse resp = (HttpServletResponse) response;
      resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
      injectionPerformedCorrectly = sheep != null;
   }
}

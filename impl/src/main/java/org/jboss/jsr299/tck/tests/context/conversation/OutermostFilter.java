/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.context.conversation;

import java.io.IOException;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.NonexistentConversationException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class OutermostFilter implements Filter
{
   @Inject 
   private Conversation conversation;
   

   public void destroy()
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      if ("foo".equals(request.getParameter("cid")))
      {
         try
         {
            chain.doFilter(request, response);
            throw new RuntimeException("Expected exception not thrown");
         }
         catch (ServletException e)
         {
            Throwable cause = e.getCause();
            while (cause != null)
            {
               if (e.getCause() instanceof NonexistentConversationException)
               {
                  response.setContentType("text/html");
                  response.getWriter().print("NonexistentConversationException thrown properly\n");
                  // FIXME WELD-878
                  // response.getWriter().print("Conversation.isTransient: " + conversation.isTransient());
                  return;
               }
               cause = e.getCause();
            }
            throw new RuntimeException("Unexpected exception thrown");
         }
      }
      else
      {
         chain.doFilter(request, response);
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
   }

}

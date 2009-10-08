package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TagLibraryListener implements ServletContextListener
{
   @Inject
   private Sheep sheep;

   public void contextDestroyed(ServletContextEvent sce)
   {
   }

   public void contextInitialized(ServletContextEvent sce)
   {
      sce.getServletContext().setAttribute("tag.library.listener.injected", sheep != null);
   }
}

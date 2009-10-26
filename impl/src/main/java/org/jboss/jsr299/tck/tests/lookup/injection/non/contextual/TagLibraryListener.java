package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TagLibraryListener implements ServletContextListener
{
   @Inject
   private Sheep sheep;
   private boolean initializerCalled = false;
   
   @Inject
   public void initialize(Sheep sheep) {
      initializerCalled = sheep != null;
   }

   public void contextDestroyed(ServletContextEvent sce)
   {
   }

   public void contextInitialized(ServletContextEvent sce)
   {
      sce.getServletContext().setAttribute("tag.library.listener.injected", sheep != null);
      sce.getServletContext().setAttribute("tag.library.listener.initializer.called", initializerCalled);
   }
}

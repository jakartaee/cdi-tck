package org.jboss.jsr299.tck.tests.context.application;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestServletContextListener implements ServletContextListener
{
   @Inject
   private BeanManager manager;
   @Inject
   private Result result;
   
   public void contextDestroyed(ServletContextEvent sce)
   {
   }

   public void contextInitialized(ServletContextEvent sce)
   {
      boolean result = manager.getContext(ApplicationScoped.class).isActive();
      this.result.setApplicationScopeActiveForServletContextListener(result);
   }

}

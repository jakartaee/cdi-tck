package org.jboss.jsr299.tck.tests.context.application;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class TestServletRequestListener implements ServletRequestListener
{

   @Inject
   private BeanManager manager;
   @Inject
   private Result result;

   public void requestDestroyed(ServletRequestEvent sre)
   {
   }

   public void requestInitialized(ServletRequestEvent sre)
   {
      boolean result = manager.getContext(ApplicationScoped.class).isActive();
      this.result.setApplicationScopeActiveForServletRequestListener(result);
   }

}

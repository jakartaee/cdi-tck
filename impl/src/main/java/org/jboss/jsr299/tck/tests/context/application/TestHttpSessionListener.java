package org.jboss.jsr299.tck.tests.context.application;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class TestHttpSessionListener implements HttpSessionListener
{
   @Inject
   private BeanManager manager;
   @Inject
   private Result result;
   
   public void sessionCreated(HttpSessionEvent hsc)
   {
      boolean result = manager.getContext(ApplicationScoped.class).isActive();
      this.result.setApplicationScopeActiveForHttpSessionListener(result);
   }

   public void sessionDestroyed(HttpSessionEvent hsc)
   {
   }

}

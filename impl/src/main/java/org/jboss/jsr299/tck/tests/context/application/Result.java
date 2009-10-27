package org.jboss.jsr299.tck.tests.context.application;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class Result
{
   private boolean applicationScopeActiveForServletContextListener = false;
   private boolean applicationScopeActiveForHttpSessionListener = false;
   private boolean applicationScopeActiveForServletRequestListener = false;
   
   public boolean isApplicationScopeActiveForServletContextListener()
   {
      return applicationScopeActiveForServletContextListener;
   }
   public void setApplicationScopeActiveForServletContextListener(boolean applicationScopeActiveForServletContextListener)
   {
      this.applicationScopeActiveForServletContextListener = applicationScopeActiveForServletContextListener;
   }
   public boolean isApplicationScopeActiveForHttpSessionListener()
   {
      return applicationScopeActiveForHttpSessionListener;
   }
   public void setApplicationScopeActiveForHttpSessionListener(boolean applicationScopeActiveForHttpSessionListener)
   {
      this.applicationScopeActiveForHttpSessionListener = applicationScopeActiveForHttpSessionListener;
   }
   public boolean isApplicationScopeActiveForServletRequestListener()
   {
      return applicationScopeActiveForServletRequestListener;
   }
   public void setApplicationScopeActiveForServletRequestListener(boolean applicationScopeActiveForServletRequestListener)
   {
      this.applicationScopeActiveForServletRequestListener = applicationScopeActiveForServletRequestListener;
   }
}

package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.ejb.Local;

@Local
public interface FMS
{
   public void turnLeft();
   
   public void turnRight();
   
   public void climb();
   
   public void descend();
   
   public boolean isApplicationScopeActive();

   public void setApplicationScopeActive(boolean applicationScopeActive);
   
   public boolean isSameBean();
}

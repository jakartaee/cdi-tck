package org.jboss.jsr299.tck.tests.context.request.ejb;

import javax.ejb.Local;

@Local
public interface FMS
{
   public void turnLeft();
   
   public void turnRight();
   
   public void climb();
   
   public void descend();
   
   public boolean isRequestScopeActive();

   public void setRequestScopeActive(boolean requestScopeActive);
   
   public boolean isSameBean();
}

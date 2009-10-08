package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Local;

@Local
public interface GoldenRetrieverLocal
{
   
   public void bye(Object something);
   
   public void anObserverMethod(String event);
   
}
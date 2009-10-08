package org.jboss.jsr299.tck.tests.event.observer.transactional;

import javax.ejb.Local;

@Local
public interface Agent
{

   public abstract void sendInTransaction(Object event);

   public abstract void sendOutsideTransaction(Object event);
   
   public void sendInTransactionAndFail(Object event) throws Exception;

}
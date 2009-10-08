package org.jboss.jsr299.tck.tests.event.observer.transactional;

import java.math.BigInteger;

import javax.ejb.Local;

@Local
public interface PomeranianInterface
{
   /**
    * Observes a String event only after the transaction is completed.
    * 
    * @param someEvent
    */
   public void observeStringEvent(String someEvent);

   /**
    * Observes an Integer event if the transaction is successfully completed.
    * 
    * @param event
    */
   public void observeIntegerEvent(Integer event);

   /**
    * Observes a Float event only if the transaction failed.
    * 
    * @param event
    */
   public void observeFloatEvent(Float event);

   public void observeBigIntegerEvent(BigInteger event);
   
   public void observeExceptionEvent(RuntimeException event);
   
   public void observeCharEvent(Character event);

   public boolean isCorrectContext();

   public void setCorrectContext(boolean correctContext);

   public boolean isCorrectTransactionState();

   public void setCorrectTransactionState(boolean correctTransactionState);
   
   public void removeSessionBean();
}

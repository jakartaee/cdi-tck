package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

@RequestScoped class StaticObserver
{
   private static boolean deliveryReceived = false;
   private static Thread threadObservingEvent = null;
   
   public static void accept(@Observes Delivery delivery)
   {
      setDeliveryReceived(true);
      setThreadObservingEvent(Thread.currentThread());
   }
   
   public static boolean isDeliveryReceived()
   {
      return deliveryReceived;
   }

   public static void setDeliveryReceived(boolean deliveryReceived)
   {
      StaticObserver.deliveryReceived = deliveryReceived;
   }
   
   public static Thread getThreadObservingEvent()
   {
      return threadObservingEvent;
   }
   
   public static void setThreadObservingEvent(Thread threadObservingEvent)
   {
      StaticObserver.threadObservingEvent = threadObservingEvent;
   }
   
   public static void reset()
   {
      deliveryReceived = false;
      threadObservingEvent = null;
   }
}

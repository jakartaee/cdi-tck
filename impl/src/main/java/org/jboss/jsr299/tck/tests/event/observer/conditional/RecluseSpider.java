package org.jboss.jsr299.tck.tests.event.observer.conditional;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

/**
 * Simple web bean that conditionally listens to events.
 *
 */
@RequestScoped
class RecluseSpider
{
   private static boolean notified = false;
   private boolean instanceNotified = false;
   private Web web;
   
   public void observe(@Observes(notifyObserver = Reception.IF_EXISTS) ConditionalEvent someEvent)
   {
      notified = true;
      instanceNotified = true;
      if (web != null)
      {
         web.addRing();
      }
   }
   
   public boolean isInstanceNotified()
   {
      return instanceNotified;
   }
   
   public static boolean isNotified()
   {
      return notified;
   }
   
   public static void reset()
   {
      notified = false;
   }
   
   public void setWeb(Web web)
   {
      this.web = web;
   }
   
   public Web getWeb()
   {
      return this.web;
   }
}

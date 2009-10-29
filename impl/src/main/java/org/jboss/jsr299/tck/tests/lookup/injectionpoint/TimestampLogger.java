package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import java.util.Date;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class TimestampLogger implements Logger
{
   @Delegate 
   private Logger logger;
   
   private static Logger staticLogger;
   
   private static String loggedMessage;

   public void log(String message)
   {
      staticLogger = logger;
      loggedMessage = message;
      logger.log(new Date().toString() + ":  " + message);
   }

   public static Logger getLogger()
   {
      return staticLogger;
   }
   
   /**
    * @return the loggedMessage
    */
   public static String getLoggedMessage()
   {
      return loggedMessage;
   }
   
   public static void reset()
   {
      loggedMessage = null;
      staticLogger = null;
   }

}

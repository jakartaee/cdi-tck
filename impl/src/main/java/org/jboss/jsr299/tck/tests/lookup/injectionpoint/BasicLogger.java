package org.jboss.jsr299.tck.tests.lookup.injectionpoint;


class BasicLogger implements Logger
{
   
   private static String message;

   public String getMessage()
   {
      return message;
   }

   public void log(String message)
   {
      BasicLogger.message = message;
   }

}

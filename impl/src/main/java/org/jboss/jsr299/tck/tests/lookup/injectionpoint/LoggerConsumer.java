package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.inject.Inject;

class LoggerConsumer
{
   @Inject
   private Logger logger;

   public Logger getLogger()
   {
      return logger;
   }

   public void doSomething()
   {
      logger.log("Test message");
   }
}

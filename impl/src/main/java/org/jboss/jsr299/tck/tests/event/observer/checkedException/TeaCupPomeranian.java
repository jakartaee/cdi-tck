package org.jboss.jsr299.tck.tests.event.observer.checkedException;

import javax.enterprise.event.Observes;

@Tame class TeaCupPomeranian
{
   public static class OversizedException extends RuntimeException
   {
      private static final long serialVersionUID = 1L;
   }
   
   public static class TooSmallException extends Exception
   {
      private static final long serialVersionUID = 1L;
   }
   
   public void observeSimpleEvent(@Observes String someEvent)
   {
      throw new OversizedException();
   }
   
   public void observeAnotherSimpleEvent(@Observes Integer someEvent) throws TooSmallException
   {
      throw new TooSmallException();
   }
}

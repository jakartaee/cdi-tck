package org.jboss.jsr299.tck.tests.deployment.lifecycle;

class NotAuthorizedException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public NotAuthorizedException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public NotAuthorizedException(String message)
   {
      super(message);
   }

}

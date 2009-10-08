package org.jboss.jsr299.tck;

/**
 * A category exception which can be declared in the @ExpectedDeploymentException
 * annotation of an @Artifact to detect a definition error as described in
 * Section 12.4, "Problems detected automatically by the container".
 * 
 * @see org.jboss.jsr299.tck.spi.Managers#isDefinitionError(Throwable)
 */
public class DefinitionError extends RuntimeException 
{

   /**
	 * 
	 */
	private static final long serialVersionUID = 3544233876045340712L;

public DefinitionError()
   {
      super();
   }

   public DefinitionError(String message, Throwable cause)
   {
      super(message, cause);
   }

   public DefinitionError(String message)
   {
      super(message);
   }

   public DefinitionError(Throwable cause)
   {
      super(cause);
   }



}

package org.jboss.jsr299.tck;

/**
 * A category exception which can be declared in the @ExpectedDeploymentException
 * annotation of an @Artifact to detect a definition error as described in
 * Section 12.4, "Problems detected automatically by the container".
 * 
 * @see org.jboss.jsr299.tck.spi.Managers#isDefinitionError(Throwable)
 */
public class DeploymentFailure extends RuntimeException 
{

   /**
	 * 
	 */
	private static final long serialVersionUID = 3544233876045340712L;

public DeploymentFailure()
   {
      super();
   }

   public DeploymentFailure(String message, Throwable cause)
   {
      super(message, cause);
   }

   public DeploymentFailure(String message)
   {
      super(message);
   }

   public DeploymentFailure(Throwable cause)
   {
      super(cause);
   }



}

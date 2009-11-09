package org.jboss.jsr299.tck;

/**
 * A category exception which can be declared in the @ExpectedDeploymentException
 * annotation of an @Artifact to detect a deployment error as described in
 * Section 12.4, "Problems detected automatically by the container".
 * 
 * @see org.jboss.jsr299.tck.spi.Managers#isDeploymentProblem(Throwable)
 */
public class DeploymentError extends RuntimeException 
{

   /**
	 * 
	 */
	private static final long serialVersionUID = 6495327942053526437L;

public DeploymentError()
   {
      super();
   }

   public DeploymentError(String message, Throwable cause)
   {
      super(message, cause);
   }

   public DeploymentError(String message)
   {
      super(message);
   }

   public DeploymentError(Throwable cause)
   {
      super(cause);
   }



}

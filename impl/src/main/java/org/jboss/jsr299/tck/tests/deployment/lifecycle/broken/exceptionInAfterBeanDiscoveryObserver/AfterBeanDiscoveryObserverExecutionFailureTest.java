package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.exceptionInAfterBeanDiscoveryObserver;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

/**
 * Tests that any exception raised in a method observing the AfterBeanDiscovery
 * event results in a definition error.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
@Extension(value="javax.enterprise.inject.spi.Extension")
public class AfterBeanDiscoveryObserverExecutionFailureTest extends AbstractJSR299Test
{
   @Test(groups={"rewrite"})
   @SpecAssertion(section = "11.5.2", id = "g")
   public void testObserverFailureTreatedAsDefinitionError()
   {
      assert false;
   }
}
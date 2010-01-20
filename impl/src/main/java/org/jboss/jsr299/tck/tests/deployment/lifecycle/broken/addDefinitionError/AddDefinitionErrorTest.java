package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDefinitionError;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

/**
 * Tests that any definition error added by an observer of the AfterBeanDiscovery
 * event results in a definition error.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@Extension("javax.enterprise.inject.spi.Extension")
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class AddDefinitionErrorTest extends AbstractJSR299Test
{
   @Test(groups={"rewrite"})
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id = "ca"),
      @SpecAssertion(section = "12.2", id = "c")
   })
   public void testObserverDefinitionErrorTreatedAsDefinitionError()
   {
      assert false;
   }

}

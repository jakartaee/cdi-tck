package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDefinitionError;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * Tests that any definition error added by an observer of the AfterBeanDiscovery
 * event results in a definition error.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class AddDefinitionErrorTest extends AbstractJSR299Test
{
   @Test(groups={"jboss-as-broken", "rewrite"})
   // WBRI-312
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id = "ca"),
      @SpecAssertion(section = "12.2", id = "c")
   })
   public void testObserverDefinitionErrorTreatedAsDefinitionError()
   {
      assert false;
   }
   
   // FIXME need to communicate state of container at the time of failure
   //   @Override
   //   @AfterClass(alwaysRun = true, groups = "scaffold")
   //   public void afterClass() throws Exception
   //   {
   //      super.afterClass();
   //      assert BeanDiscoveryObserver.getInvocationCount() == 2 : "All AfterBeanDiscovery observer methods should have been called";
   //   }
}

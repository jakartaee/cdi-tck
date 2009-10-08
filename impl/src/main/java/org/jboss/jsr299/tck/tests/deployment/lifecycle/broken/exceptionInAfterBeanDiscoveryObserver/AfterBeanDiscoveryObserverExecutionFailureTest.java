package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.exceptionInAfterBeanDiscoveryObserver;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * Tests that any exception raised in a method observing the AfterBeanDiscovery
 * event results in a definition error.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="PFD2")
public class AfterBeanDiscoveryObserverExecutionFailureTest extends AbstractJSR299Test
{
   @Test(groups={"jboss-as-broken", "rewrite"})
   //TODO Needs Extension stuff adding
   // WBRI-312
   @SpecAssertion(section = "11.5.2", id = "g")
   public void testObserverFailureTreatedAsDefinitionError()
   {
      assert false;
   }
}
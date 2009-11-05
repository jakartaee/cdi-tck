package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringBeanDiscovery;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091101")
public class DeploymentFailureTest extends AbstractJSR299Test
{
   
   // TODO make this an integration test using Extension
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "10.4.2", id = "e"),
      @SpecAssertion(section = "11.5.2", id = "a")
   })
   public void testDeploymentFailsBeforeNotifyingObserversAfterBeanDiscovery()
   {
      assert false;
   }

}

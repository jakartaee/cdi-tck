package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle.unproxyable;

import javax.enterprise.inject.UnproxyableResolutionException;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(UnproxyableResolutionException.class)
@SpecVersion(spec="cdi", version="20091018")
public class UnproxyableManagedBeanTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.5.3", id = "a")
   public void testNormalScopedUnproxyableBeanThrowsException()
   {
      assert false;
   }
}

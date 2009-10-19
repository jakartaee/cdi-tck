package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonSerializableInterceptorClass;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091018")
public class ManagedBeanWithNonSerializableInterceptorClassTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "passivation"})
   @SpecAssertion(section = "6.6.1", id = "bb")
   // WBRI-361
   public void testManagedBeanWithNonSerializableInterceptorClassNotOK()
   {
      assert false;
   } 
}

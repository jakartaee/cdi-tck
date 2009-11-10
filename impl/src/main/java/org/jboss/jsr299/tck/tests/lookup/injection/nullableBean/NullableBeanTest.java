package org.jboss.jsr299.tck.tests.lookup.injection.nullableBean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class NullableBeanTest extends AbstractJSR299Test
{
   @Test(groups = { "injection", "producerMethod" })
   @SpecAssertion(section = "5.2.4", id = "aa")
   public void testPrimitiveInjectionPointResolvesToNullableWebBean() throws Exception
   {
      assert false;
   }
   
}

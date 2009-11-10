package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.statelessWithApplicationScope;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class StatelessWithApplicationScopeTest extends AbstractJSR299Test
{   
   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2", id = "da")
   public void testStatelessWithSessionScopeFails()
   {
      assert false;
   }

   
}

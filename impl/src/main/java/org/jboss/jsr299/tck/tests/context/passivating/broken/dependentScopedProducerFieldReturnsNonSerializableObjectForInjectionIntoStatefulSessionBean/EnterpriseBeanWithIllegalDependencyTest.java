package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerFieldReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import javax.enterprise.inject.IllegalProductException;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@ExpectedDeploymentException(IllegalProductException.class)
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseBeanWithIllegalDependencyTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "passivation", "integration"})
   @SpecAssertion(section = "6.6.4", id = "fb")
   public void test()
   {
      assert false;
   }
}

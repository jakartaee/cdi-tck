package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.methodOnSessionBean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="PFD2")
public class DisposalMethodOnSessionBean extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.3.5", id = "d"),
      @SpecAssertion(section = "3.3.7", id = "fa")
   })
   public void testDisposalMethodNotBusinessOrStatic()
   {
      assert false;
   }

}

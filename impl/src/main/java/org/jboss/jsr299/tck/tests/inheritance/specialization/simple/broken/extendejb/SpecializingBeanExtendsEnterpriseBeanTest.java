package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.extendejb;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="PFD2")
public class SpecializingBeanExtendsEnterpriseBeanTest extends AbstractJSR299Test
{
   @Test(groups = { "specialization" })
   @SpecAssertion(section = "3.1.4", id = "dc")
   public void testSpecializingClassDirectlyExtendsEnterpriseBean()
   {
      assert false;
   }


}

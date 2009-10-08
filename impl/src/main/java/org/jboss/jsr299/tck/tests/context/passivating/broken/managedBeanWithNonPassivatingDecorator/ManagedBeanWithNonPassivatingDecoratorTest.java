package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonPassivatingDecorator;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="PFD2")
@BeansXml("beans.xml")
public class ManagedBeanWithNonPassivatingDecoratorTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "passivation", "integration"})
   @SpecAssertion(section = "6.6.4", id = "ha")
   public void testManagedBeanWithNonPassivatingDecoratorFails()
   {
      assert false;
   }
}

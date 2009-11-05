package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.names;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091101")
public class SpecializingAndSpecializedBeanHasNameTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "4.3.1", id = "lb")
   public void testSpecializingAndSpecializedBeanHasName()
   {
      assert false;
   }
}

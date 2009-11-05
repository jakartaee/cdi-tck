package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.specializingAndSpecializedBeanHaveName;


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
public class SpecializingAndSpecializedBeanHaveNameTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "4.3.1", id = "la")
   public void testSpecializingAndSpecializedBeanHasName()
   {
      assert false;
   }
   
}

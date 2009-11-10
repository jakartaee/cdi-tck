package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.broken.parameterizedReturnTypeWithWildcard;


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
public class ParameterizedReturnTypeWithWildcardTest extends AbstractJSR299Test
{
   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4", id = "ga")
   public void testParameterizedReturnTypeWithWildcard()
   {
      assert false;
   }
   
   
   
}

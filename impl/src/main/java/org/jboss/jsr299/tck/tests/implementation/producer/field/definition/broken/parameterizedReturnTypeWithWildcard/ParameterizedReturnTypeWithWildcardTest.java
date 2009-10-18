package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.broken.parameterizedReturnTypeWithWildcard;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class ParameterizedReturnTypeWithWildcardTest extends AbstractJSR299Test
{
   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4", id = "ga")
   public void testParameterizedReturnTypeWithWildcard()
   {
      assert false;
   }
   
   
   
}

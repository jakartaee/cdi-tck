package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterizedTypeWithWildcard;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class ParameterizedTypeWithWildcardTest extends AbstractJSR299Test
{
  
   @Test(groups = "producerMethod")
   @SpecAssertions({
      @SpecAssertion(section = "3.3", id = "ha"),
      @SpecAssertion(section = "2.2.1", id="lb")
   })
   public void testParameterizedReturnTypeWithWildcard() throws Exception
   {
      assert false;
   }
   
}

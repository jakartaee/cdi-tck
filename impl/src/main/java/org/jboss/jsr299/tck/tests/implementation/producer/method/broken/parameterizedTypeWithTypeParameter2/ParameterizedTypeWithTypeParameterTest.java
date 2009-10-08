package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.parameterizedTypeWithTypeParameter2;


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
@SpecVersion(spec="cdi", version="PFD2")
public class ParameterizedTypeWithTypeParameterTest extends AbstractJSR299Test
{   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.3", id = "ib"),
      @SpecAssertion(section = "2.2.1", id="la")
   })
   public void testParameterizedType()
   {
      assert false;
   }

}

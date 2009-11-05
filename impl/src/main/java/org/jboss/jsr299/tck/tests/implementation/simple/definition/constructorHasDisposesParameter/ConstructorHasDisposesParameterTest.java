package org.jboss.jsr299.tck.tests.implementation.simple.definition.constructorHasDisposesParameter;


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
@SpecVersion(spec="cdi", version="20091101")
public class ConstructorHasDisposesParameterTest extends AbstractJSR299Test
{
   @Test(groups = { "disposalMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "3.7.1", id = "da"),
      @SpecAssertion(section = "12.2", id="db"),
      @SpecAssertion(section = "12.4", id= "a")
   })
   public void testConstructorHasDisposesParameter() throws Exception
   {
      assert false;
   }
   
}

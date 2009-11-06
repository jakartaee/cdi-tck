package org.jboss.jsr299.tck.tests.implementation.simple.definition.tooManyInitializerAnnotatedConstructors;


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
public class TooManyInitializerAnnotatedConstructorsTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertion(section = "3.7.1", id = "ca")
   public void testTooManyInitializerAnnotatedConstructor()
   {
      assert false;
   }
   
}

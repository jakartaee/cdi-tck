package org.jboss.jsr299.tck.tests.implementation.initializer.broken.parameterAnnotatedDisposes;


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
public class ParameterAnnotatedDisposesTest extends AbstractJSR299Test
{   
   @Test(groups = "initializerMethod")
   @SpecAssertion(section = "3.9.1", id = "ca")
   public void testInitializerMethodHasParameterAnnotatedDisposes()
   {
      assert false;
   }
   
}

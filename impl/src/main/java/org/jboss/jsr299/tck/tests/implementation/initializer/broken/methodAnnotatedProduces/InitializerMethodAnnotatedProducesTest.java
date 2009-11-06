package org.jboss.jsr299.tck.tests.implementation.initializer.broken.methodAnnotatedProduces;


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
public class InitializerMethodAnnotatedProducesTest extends AbstractJSR299Test
{
   
   @Test(groups = "initializerMethod")
   @SpecAssertions( { @SpecAssertion(section = "3.9.1", id = "ba"), 
	   @SpecAssertion(section = "3.3.2", id = "ca") })
   public void testInitializerMethodAnnotatedProduces()
   {
      assert false;
   }
   
}

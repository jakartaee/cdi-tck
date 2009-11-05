package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.initializerUnallowed;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class InitializerUnallowedDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "3.3.6", id = "da")
   public void testInitializerUnallowed()
   {
      assert false;
   }

}

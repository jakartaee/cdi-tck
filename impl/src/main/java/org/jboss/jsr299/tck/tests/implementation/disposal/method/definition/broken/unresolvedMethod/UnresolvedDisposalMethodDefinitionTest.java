package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.unresolvedMethod;

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
@SpecVersion(spec="cdi", version="PFD2")
public class UnresolvedDisposalMethodDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "3.3.9", id = "ca")
   public void testUnresolvedDisposalMethod()
   {
      assert false;
   }
}

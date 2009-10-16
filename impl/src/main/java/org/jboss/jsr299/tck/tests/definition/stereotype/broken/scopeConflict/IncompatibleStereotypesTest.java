package org.jboss.jsr299.tck.tests.definition.stereotype.broken.scopeConflict;

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
public class IncompatibleStereotypesTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "2.4.4", id = "da") 
   })
   public void testMultipleIncompatibleScopeStereotypes()
   {
      assert false;
   }
}

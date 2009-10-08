package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.statelessWithConversationScope;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="PFD2")
public class StatelessWithConversationScopeTest extends AbstractJSR299Test
{
   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2", id = "da")
   public void testStatelessWithConversationScopeFails()
   {
      assert false;
   }
   
}

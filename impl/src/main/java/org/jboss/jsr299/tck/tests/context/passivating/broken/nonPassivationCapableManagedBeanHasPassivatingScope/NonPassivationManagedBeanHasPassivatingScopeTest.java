package org.jboss.jsr299.tck.tests.context.passivating.broken.nonPassivationCapableManagedBeanHasPassivatingScope;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author David Allen
 * 
 */
@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091101")
public class NonPassivationManagedBeanHasPassivatingScopeTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "passivation" })
   @SpecAssertion(section = "6.6.4", id = "aaa")
   public void testSimpleWebBeanWithNonSerializableImplementationClassFails()
   {
      assert false;
   }
}

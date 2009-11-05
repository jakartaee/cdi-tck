package org.jboss.jsr299.tck.tests.context.passivating.broken.nonPassivationCapableProducerField;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * 
 * @author Shane Bryzak
 */
@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
@ExpectedDeploymentException(DeploymentError.class)
public class NonPassivationCapableProducerFieldTest extends AbstractJSR299Test
{
   @Test(groups = { "passivation", "ri-wrong-cause" })
   @SpecAssertions({
     @SpecAssertion(section = "6.6.4", id = "d")
   })
   // TODO RI is erroring, but for the wrong reason
   public void testNonPassivationCapableProducerFieldNotOk()
   {
      assert false;
   }
}

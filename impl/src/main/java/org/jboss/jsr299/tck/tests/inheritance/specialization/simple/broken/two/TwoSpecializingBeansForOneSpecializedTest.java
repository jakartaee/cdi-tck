package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.two;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean.TwoBeansSpecializeTheSameBeanTest;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="PFD2")
public class TwoSpecializingBeansForOneSpecializedTest extends AbstractJSR299Test
{
   /**
    * This is for managed beans
    * 
    * @see TwoBeansSpecializeTheSameBeanTest#testTwoBeansSpecializeTheSameBean()
    * @see org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean.TwoBeansSpecializeTheSameBeanTest#testTwoBeansSpecializeTheSameBean()
    * 
    */
   //@Test
   //@SpecAssertion(section="4.3.3", id = "c") section removed from spec
   public void testTwoBeansSpecializeTheSameBean()
   {
      assert false;
   }

}

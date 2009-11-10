package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.two.TwoSpecializingBeansForOneSpecializedTest;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;

@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091018")
public class TwoBeansSpecializeTheSameBeanTest extends AbstractJSR299Test
{

   /**
    * This method tests producers
    * 
    * @see org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean.TwoBeansSpecializeTheSameBeanTest#testTwoBeansSpecializeTheSameBean()
    * @see TwoSpecializingBeansForOneSpecializedTest#testTwoBeansSpecializeTheSameBean()
    */
   //@Test
   //@SpecAssertion(section = "4.3.3", id = "c") removed from spec
   public void testTwoBeansSpecializeTheSameBean()
   {
      assert false;
   }

}
